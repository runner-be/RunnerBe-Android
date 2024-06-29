package com.applemango.runnerbe.presentation.screen.compose.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.LoginType
import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.screen.activity.HomeActivity
import com.applemango.runnerbe.presentation.screen.compose.ui.theme.aggro
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback



@Composable
fun LoginView(
    navController: NavController
) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val mContext = LocalContext.current as ComponentActivity
    val isTokenCheck = viewModel.isTokenLogin.observeAsState()
    var isLoginViewVisible = !(isTokenCheck.value ?: false)
    viewModel.isTokenCheck()
    isTokenCheck.value?.let {
        isLoginViewVisible = !it
        if (it) {
            mContext.startActivity(Intent(mContext, HomeActivity::class.java))
            mContext.finish()
        }
    }

    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.black_background))
            .fillMaxSize()
    ) {
        LogoAndTextView(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(bottom = 165.dp)
        )
        if (isLoginViewVisible) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                KakaoLoginView(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                NaverLoginView(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    navController
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun LogoAndTextView(modifier: Modifier) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val mContext = LocalContext.current as ComponentActivity
    LaunchedEffect(viewModel.isSocialLogin) {
        viewModel.isSocialLogin.collect {
            when (it) {
                is CommonResponse.Success<*> -> {
                    mContext.startActivity(Intent(mContext, HomeActivity::class.java))
                    mContext.finish()
                }
            }
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_runnerbe),
            contentDescription = "logo"
        )
        Text(
            stringResource(id = R.string.runner_be),
            color = colorResource(id = R.color.primary),
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            fontFamily = aggro,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun KakaoLoginView(modifier: Modifier) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val mContext = LocalContext.current as ComponentActivity

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            val errorMessage = when (error.toString()) {
                AuthErrorCause.AccessDenied.toString() -> "접근이 거부 됨(동의 취소)"
                AuthErrorCause.InvalidClient.toString() -> "유효하지 않은 앱"
                AuthErrorCause.InvalidGrant.toString() -> "인증 수단이 유효하지 않아 인증할 수 없는 상태"
                AuthErrorCause.InvalidRequest.toString() -> "요청 파라미터 오류"
                AuthErrorCause.InvalidScope.toString() -> "유효하지 않은 scope ID"
                AuthErrorCause.Misconfigured.toString() -> "설정이 올바르지 않음(android key hash)"
                AuthErrorCause.ServerError.toString() -> "서버 내부 에러"
                AuthErrorCause.Unauthorized.toString() -> "앱이 요청 권한이 없음"
                else -> {
                    error.toString()
                }// Unknown

            }
            Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            Log.d("kakao_token", token.accessToken)
            val request = SocialLoginRequest(token.accessToken)
            viewModel.login(LoginType.KAKAO, request)
        }
    }

    Button(
        onClick = {

            /* 카카오 로그인 기능 */
            var keyHash = Utility.getKeyHash(mContext)
            Log.i("keyHash", keyHash)
            KakaoSdk.init(mContext, mContext.getString(R.string.kakao_native_key))

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
                UserApiClient.instance.loginWithKakaoTalk(mContext, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
            }

        },
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.kakao_yellow)),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_kakao_logo),
                contentDescription = "kakao logo",
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = stringResource(id = R.string.login_kakao),
                color = colorResource(id = R.color.black_19),
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun NaverLoginView(modifier: Modifier, navController: NavController) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val mContext = LocalContext.current as ComponentActivity
    Button(
        onClick = {

            /* 네이버 로그인 기능 */
            var naverToken: String
            NaverIdLoginSDK.initialize(
                mContext,
                mContext.getString(R.string.login_naver_client_id),
                mContext.getString(R.string.login_naver_client_secret),
                mContext.getString(R.string.app_name)
            )

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode()
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        mContext,
                        "errorCode: ${errorCode}\nerrorDescription: $errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess() {
                    // 네이버 로그인 인증이 성공할 경우
                    naverToken = NaverIdLoginSDK.getAccessToken().toString()
                    Log.d("naver_token", naverToken)
                    val request = SocialLoginRequest(naverToken)
                    viewModel.login(LoginType.NAVER, request)
                }
            }

            NaverIdLoginSDK.authenticate(mContext, oauthLoginCallback)
        },
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.naver_green)),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_naver_logo),
                contentDescription = "naver logo",
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = stringResource(id = R.string.login_naver),
                color = colorResource(id = R.color.white),
                modifier = Modifier.align(Alignment.TopCenter),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun LogoPreView() {
    LoginView(rememberNavController())
}