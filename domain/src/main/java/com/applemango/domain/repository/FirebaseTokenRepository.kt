package com.applemango.domain.repository

interface FirebaseTokenRepository {
    suspend fun updateFirebaseToken()
}