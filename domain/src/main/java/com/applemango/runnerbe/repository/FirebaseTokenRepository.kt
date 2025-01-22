package com.applemango.runnerbe.repository

interface FirebaseTokenRepository {
    suspend fun updateToken(userId: Int, token: String)
}