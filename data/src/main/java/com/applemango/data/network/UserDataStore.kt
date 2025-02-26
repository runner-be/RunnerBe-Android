package com.applemango.data.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.applemango.data.dto.LoginType
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val Context.userDataStore by preferencesDataStore(name = "runnerBe_user")

class UserDataStore @Inject constructor(
    context: Context
) {
    private val dataStore = context.userDataStore
    private var cachedJwtToken: String? = null

    // Preferences Keys 정의
    private val TOKEN_KEY = stringPreferencesKey("X-ACCESS-TOKEN")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh-token")
    private val DEVICE_TOKEN_KEY = stringPreferencesKey("device-token")
    private val USER_ID_KEY = intPreferencesKey("userId")
    private val UUID_KEY = stringPreferencesKey("uuid")
    private val LOGIN_TYPE_KEY = intPreferencesKey("loginType")
    private val RUNNING_PACE_KEY = stringPreferencesKey("runningPace")

    suspend fun logoutSet() {
        if (getLoginType().first() == LoginType.KAKAO) {
            UserApiClient.instance.logout { e ->
                e?.printStackTrace()
            }
        } else {
            NaverIdLoginSDK.logout()
        }
//        if(isKakao()) {}//카카오도 SDK 로그아웃이 있는 경우 처리하기
        runBlocking(Dispatchers.IO) {
            cachedJwtToken = null
            removeUserId()
            removeLoginType()
            removePace()
            removeToken()
            removeDeviceToken()
            removeRefreshToken()
            removeUuid()
        }
    }

    fun getUserId(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: -1
    }.flowOn(Dispatchers.IO)

    suspend fun getJwtToken(): String? {
        if (cachedJwtToken == null) {
            val token = dataStore.data
                .map { preferences -> preferences[TOKEN_KEY] }
                .flowOn(Dispatchers.IO)
                .first()
            cachedJwtToken = token
        }
        return cachedJwtToken
    }

    private fun getLoginType(): Flow<LoginType?> = dataStore.data.map { preferences ->
        if (preferences[LOGIN_TYPE_KEY] == 1) {
            LoginType.KAKAO
        } else {
            LoginType.NAVER
        }
    }.flowOn(Dispatchers.IO)

    fun getPace(): Flow<String> = dataStore.data.map { preferences ->
        preferences[RUNNING_PACE_KEY] ?: "no_pace_registered"
    }

    fun getDeviceToken(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[DEVICE_TOKEN_KEY]
    }

    fun getUuid(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[UUID_KEY]
    }

    suspend fun setUserId(userId: Int) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[USER_ID_KEY] = userId
            }
        }
    }

    suspend fun setJwtToken(token: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
            }
        }
    }

    suspend fun setDeviceToken(deviceToken: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[DEVICE_TOKEN_KEY] = deviceToken
            }
        }
    }

    suspend fun setRefreshToken(refreshToken: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[REFRESH_TOKEN_KEY] = refreshToken
            }
        }
    }

    suspend fun setUuid(uuid: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[UUID_KEY] = uuid
            }
        }
    }

    suspend fun setLoginType(loginValue: Int) {
        val login = if (loginValue == 1) LoginType.KAKAO else LoginType.NAVER
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[LOGIN_TYPE_KEY] = login.value
            }
        }
    }

    suspend fun setRunningPace(pace: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[RUNNING_PACE_KEY] = pace
            }
        }
    }

    private suspend fun removeUserId() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(USER_ID_KEY)
            }
        }
    }

    private suspend fun removeToken() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(TOKEN_KEY)
            }
        }
    }

    private suspend fun removeDeviceToken() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(DEVICE_TOKEN_KEY)
            }
        }
    }

    private suspend fun removeRefreshToken() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(REFRESH_TOKEN_KEY)
            }
        }
    }

    private suspend fun removeUuid() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(UUID_KEY)
            }
        }
    }

    private suspend fun removeLoginType() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(LOGIN_TYPE_KEY)
            }
        }
    }

    private suspend fun removePace() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(RUNNING_PACE_KEY)
            }
        }
    }
}