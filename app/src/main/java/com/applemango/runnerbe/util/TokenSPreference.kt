package com.applemango.runnerbe.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.applemango.runnerbe.presentation.model.LoginType

class TokenSPreference(private val applicationContext : Context) {
    val sSharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("runnerBe", Application.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sSharedPreferences.edit()

    fun isNaver() : Boolean = getLoginType() == LoginType.NAVER
    fun isKakao() : Boolean = getLoginType() == LoginType.KAKAO

    fun logoutSet() {
//        if(isNaver()) NaverIdLoginSDK.logout() //요긴 네이버 쪽 버그로 인해 로그아웃이 안되는 이슈가...
//        if(isKakao()) {}//카카오도 SDK 로그아웃이 있는 경우 처리하기
        removeToken()
        removeRefreshToken()
        removeUserId()
        removeUuid()
        removeLoginType()
    }
    fun setToken(token : String) {
        editor.putString("X-ACCESS-TOKEN", token).commit()
    }

    fun getToken(): String? {
        return sSharedPreferences.getString("X-ACCESS-TOKEN", "")
    }

    fun removeToken() {
        editor.remove("X-ACCESS-TOKEN").commit()
    }

    fun getDeviceToken() : String? {
        return sSharedPreferences.getString("deviceToken", null)
    }

    fun setDeviceToken(deviceToken : String) {
        editor.putString("deviceToken", deviceToken).commit()
    }

    fun setRefreshToken(refreshToken : String) {
        editor.putString("refresh-token",refreshToken).commit()
    }

    fun getRefreshToken() : String? {
        return sSharedPreferences.getString("refresh-token", "")
    }

    fun removeRefreshToken() {
        editor.remove("refresh-token").commit()
    }
    fun setUserId(userId : Int) {
        editor.putInt("userId", userId).commit()
    }

    fun getUserId() : Int {
        return sSharedPreferences.getInt("userId", -1)
    }

    fun removeUserId() {
        editor.remove("userId").commit()
    }

    fun setUuid(uuid: String) {
        editor.putString("uuid", uuid).commit()
    }

    fun getUuid() :String? {
        return sSharedPreferences.getString("uuid","")
    }

    fun removeUuid() {
        editor.remove("uuid").commit()
    }

    fun setLoginType(type : LoginType) {
        editor.putInt("loginType", type.value).commit()
    }

    fun getLoginType() = LoginType.findByValue(sSharedPreferences.getInt("loginType", -1))

    fun removeLoginType() {
        editor.remove("loginType").commit()
    }

    fun getMyRunningPace(): String? = sSharedPreferences.getString("runningPace", "")
    fun setMyRunningPace(pace: String) {
        editor.putString("runningPace", pace).commit()
    }
}