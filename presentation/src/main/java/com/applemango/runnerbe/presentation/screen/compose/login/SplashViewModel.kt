package com.applemango.runnerbe.presentation.screen.compose.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.usecaseImpl.user.GetUserDataUseCase
import com.applemango.runnerbe.presentation.model.type.LoginType
import com.applemango.runnerbe.repository.FirebaseTokenRepository
import com.applemango.runnerbe.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.runnerbe.usecaseImpl.user.KakaoLoginUseCase
import com.applemango.runnerbe.usecaseImpl.user.NaverLoginUseCase
import com.applemango.runnerbe.usecaseImpl.user.UpdateFirebaseTokenUseCase
import com.applemango.runnerbe.usecaseImpl.user.UpdateUserPaceUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.UpdateJwtTokenUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.UpdateLoginTypeUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.UpdateUserIdUseCase
import com.applemango.runnerbe.usecaseImpl.user.local.UpdateUuidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val naverLoginUseCase: NaverLoginUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateUserPaceUseCase: UpdateUserPaceUseCase,
    private val updateUuidUseCase: UpdateUuidUseCase,
    private val updateUserIdUseCase: UpdateUserIdUseCase,
    private val updateJwtTokenUseCase: UpdateJwtTokenUseCase,
    private val updateLoginTypeUseCase: UpdateLoginTypeUseCase,
    private val updateFirebaseTokenUseCase: UpdateFirebaseTokenUseCase
) : ViewModel() {

    private val _isTokenLogin: MutableLiveData<Boolean> = MutableLiveData()
    val isTokenLogin: LiveData<Boolean> = _isTokenLogin

    private var _isSocialLogin: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isSocialLogin : StateFlow<Boolean> = _isSocialLogin

    //UI 동작 확인용 테스트 코드
    fun isTokenCheck() {
        viewModelScope.launch {
            val userId = getUserIdUseCase()
//            val uuid = RunnerBeApplication.mTokenPreference.getUuid()

            if (userId == -1) {
                _isTokenLogin.postValue(false)
            } else {
                _isTokenLogin.postValue(true)
            }
        }
    }

    fun login(type : LoginType, accessToken: String) = viewModelScope.launch  {
        try {
            val repo = when (type) {
                LoginType.KAKAO -> kakaoLoginUseCase(accessToken)
                LoginType.NAVER -> naverLoginUseCase(accessToken)
            }

            repo.collect { result ->
                val loginResult = result.login

                val updateLoginTypeDeferred = async { updateLoginTypeUseCase(type.value) }
                val updateJwtTokenDeferred = async { loginResult.jwt?.let { updateJwtTokenUseCase(it) } }
                val updateUserIdDeferred = async { loginResult.userId?.let { updateUserIdUseCase(it) } }
                val updateUuidDeferred = async { loginResult.uuid?.let { updateUuidUseCase(it) } }

                val userDeferred = async { loginResult.userId?.let { getUserDataUseCase().first().userInfo } }

                updateLoginTypeDeferred.await()
                updateJwtTokenDeferred.await()
                updateUserIdDeferred.await()
                updateUuidDeferred.await()

                val user = userDeferred.await()
                user?.pace?.let { updateUserPaceUseCase(it) }

                updateFirebaseTokenUseCase()

                _isSocialLogin.value = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isSocialLogin.value = false
        }
    }
}