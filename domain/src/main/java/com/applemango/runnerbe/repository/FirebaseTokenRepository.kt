package com.applemango.runnerbe.repository

interface FirebaseTokenRepository {
    suspend fun updateFirebaseToken()
}