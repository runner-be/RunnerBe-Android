package com.applemango.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.applemango.presentation.R
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RunnerBeApplication: Application() {

    //두루 코드 사용
    companion object {
        // 만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        // JWT Token Header 키 값
        val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"

        // refresh_token
        val refresh_token = "refresh-token"
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(
            this,
            getString(R.string.login_naver_client_id),
            getString(R.string.login_naver_client_secret),
            getString(R.string.app_name)
        )
        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // fire base settings
        FirebaseApp.initializeApp(this)
    }
}