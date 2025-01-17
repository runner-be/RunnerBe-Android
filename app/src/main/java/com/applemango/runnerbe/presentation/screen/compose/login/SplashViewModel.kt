package com.applemango.runnerbe.presentation.screen.compose.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.usecaseImpl.user.GetUserDataUseCase
import com.applemango.runnerbe.presentation.model.type.LoginType
import com.applemango.runnerbe.usecaseImpl.user.KakaoLoginUseCase
import com.applemango.runnerbe.usecaseImpl.user.NaverLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val naverLoginUseCase: NaverLoginUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _isTokenLogin: MutableLiveData<Boolean> = MutableLiveData()
    val isTokenLogin: LiveData<Boolean> = _isTokenLogin

    private var _isSocialLogin: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isSocialLogin : StateFlow<Boolean> = _isSocialLogin

    //UI 동작 확인용 테스트 코드
    fun isTokenCheck() {
        viewModelScope.launch {
            // userId = -1, uuid = ""
            val userId = RunnerBeApplication.mTokenPreference.getUserId()
            val uuid = RunnerBeApplication.mTokenPreference.getUuid()

            if (userId == -1 && uuid.isNullOrEmpty()) {
                _isTokenLogin.postValue(false)
            } else {
                _isTokenLogin.postValue(true)
            }
        }
    }

    fun login(type : LoginType, body: SocialLoginRequest) = viewModelScope.launch  {
        runCatching {
            when(type) {
                LoginType.KAKAO -> kakaoLoginUseCase(body.accessToken)
                LoginType.NAVER -> naverLoginUseCase(body.accessToken)
            }
        }.onSuccess { repo ->
            repo.catch {
                _isSocialLogin.value = false
                it.printStackTrace()
            }.collect {
                val result = it.login
                RunnerBeApplication.mTokenPreference.apply {
                    setLoginType(type)
                    result.jwt?.let { token -> setToken(token) }
                    result.userId?.let { id ->
                        setUserId(id)
                        getUserData(id)
                    }
                    result.uuid?.let { uuid ->
                        setUuid(uuid)
                    }
                }
                _isSocialLogin.value = true
            }
        }
    }

    private fun getUserData(userId: Int) = CoroutineScope(Dispatchers.IO).launch {
        getUserDataUseCase(userId).collect {
            kotlin.runCatching {
                val result = it.userInfo?.pace
                RunnerBeApplication.mTokenPreference.setMyRunningPace(result ?: "")
            }
        }
    }
}