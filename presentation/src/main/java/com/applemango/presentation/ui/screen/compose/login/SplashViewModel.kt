package com.applemango.presentation.ui.screen.compose.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.entity.SocialLoginEntity
import com.applemango.domain.usecaseImpl.user.GetUserDataUseCase
import com.applemango.domain.usecaseImpl.user.KakaoLoginUseCase
import com.applemango.domain.usecaseImpl.user.NaverLoginUseCase
import com.applemango.domain.usecaseImpl.user.UpdateFirebaseTokenUseCase
import com.applemango.domain.usecaseImpl.user.UpdateUserPaceUseCase
import com.applemango.domain.usecaseImpl.user.local.GetJwtTokenUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.domain.usecaseImpl.user.local.UpdateJwtTokenUseCase
import com.applemango.domain.usecaseImpl.user.local.UpdateLoginTypeUseCase
import com.applemango.domain.usecaseImpl.user.local.UpdateUserIdUseCase
import com.applemango.domain.usecaseImpl.user.local.UpdateUuidUseCase
import com.applemango.presentation.ui.model.type.LoginType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
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
    private val getJwtTokenUseCase: GetJwtTokenUseCase,
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
            val deferredUserId: Deferred<Int> = async { getUserIdUseCase() }
            val deferredJwtToken: Deferred<String?> = async {
                getJwtTokenUseCase()
            }
//            val uuid = RunnerBeApplication.mTokenPreference.getUuid()
            val userId = deferredUserId.await()
            val jwtToken = deferredJwtToken.await()

            if (userId != -1 && !jwtToken.isNullOrBlank()) {
                _isTokenLogin.postValue(true)
            } else {
                _isTokenLogin.postValue(false)
            }
        }
    }

    fun login(type : LoginType, accessToken: String) = viewModelScope.launch  {
        try {
            val repo: Flow<SocialLoginEntity> = when (type) {
                LoginType.KAKAO -> kakaoLoginUseCase(accessToken)
                LoginType.NAVER -> naverLoginUseCase(accessToken)
            }

            repo.collect { result ->
                val loginResult = result.login

                val updateLoginTypeDeferred = async { updateLoginTypeUseCase(type.value) }
                val updateJwtTokenDeferred = async { loginResult.jwt?.let { updateJwtTokenUseCase(it) } }
                val updateUserIdDeferred = async { loginResult.userId?.let { updateUserIdUseCase(it) } }
                val updateUuidDeferred = async { loginResult.uuid?.let { updateUuidUseCase(it) } }

                updateLoginTypeDeferred.await()
                updateJwtTokenDeferred.await()
                updateUserIdDeferred.await()
                updateUuidDeferred.await()

                // 로그인 결과의 jwtToken을 DataStore에 저장하고, interceptor에서 추후 요청의 Header에 해당 Token을 추가해야 하므로
                // update 작업이 완료된 이후에 사용자 정보 조회 API 호출
                val userDeferred = async { loginResult.userId?.let { getUserDataUseCase().first().userInfo } }
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

    override fun onCleared() {
        super.onCleared()
        _isTokenLogin.value = false
    }
}