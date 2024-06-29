package com.applemango.runnerbe.presentation.screen.compose.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.data.network.response.UserDataResponse
import com.applemango.runnerbe.domain.repository.KakaoLoginRepository
import com.applemango.runnerbe.domain.repository.NaverLoginRepository
import com.applemango.runnerbe.domain.usecase.GetUserDataUseCase
import com.applemango.runnerbe.presentation.model.LoginType
import com.applemango.runnerbe.presentation.state.CommonResponse
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
    private val kakaoRepo: KakaoLoginRepository,
    private val naverRepo: NaverLoginRepository,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _isTokenLogin: MutableLiveData<Boolean> = MutableLiveData()
    val isTokenLogin: LiveData<Boolean> = _isTokenLogin

    private var _isSocialLogin: MutableStateFlow<CommonResponse> =
        MutableStateFlow(CommonResponse.Empty)
    val isSocialLogin : StateFlow<CommonResponse> = _isSocialLogin


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
                LoginType.KAKAO -> kakaoRepo.getData(body)
                LoginType.NAVER -> naverRepo.getData(body)
            }
        }.onSuccess { repo ->
            repo.catch {
                _isSocialLogin.value = CommonResponse.Failed(999,it.message?:"error")
                it.printStackTrace()
            }.collect {
                if(it.isSuccess) {
                    val result = it.result
                    RunnerBeApplication.mTokenPreference.setLoginType(type)
                    if(result.jwt != null) RunnerBeApplication.mTokenPreference.setToken(result.jwt)
                    // 추가정보 입력시
                    result.userId?.let { userId ->
                        RunnerBeApplication.mTokenPreference.setUserId(userId)
                        getUserData(userId)
                    }
                    // 추가정보 미입력시
                    result.uuid?.let { it1 -> RunnerBeApplication.mTokenPreference.setUuid(it1) }

                    _isSocialLogin.value = CommonResponse.Success(it.code, it)
                } else _isSocialLogin.value = CommonResponse.Failed(it.code,it.message?:"error")
            }
        }
    }

    private fun getUserData(userId: Int) = CoroutineScope(Dispatchers.IO).launch {
        getUserDataUseCase(userId).collect {
            when (it) {
                is CommonResponse.Success<*> -> {
                    if (it.body is UserDataResponse) {
                        val result = it.body.result
                        RunnerBeApplication.mTokenPreference.setMyRunningPace(result.userInfo.pace?:"")
                    }
                }
            }
        }
    }
}