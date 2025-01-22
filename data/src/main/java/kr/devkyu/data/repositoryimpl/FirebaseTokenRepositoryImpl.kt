package kr.devkyu.data.repositoryimpl

import com.applemango.runnerbe.repository.FirebaseTokenRepository
import kr.devkyu.data.network.api.UpdateFirebaseTokenApi
import kr.devkyu.data.network.request.FirebaseTokenUpdateRequest
import javax.inject.Inject

class FirebaseTokenRepositoryImpl @Inject constructor(
    private val updateFirebaseTokenApi: UpdateFirebaseTokenApi
): FirebaseTokenRepository {
    override suspend fun updateToken(userId: Int, token: String) {
        updateFirebaseTokenApi.firebaseTokenUpdate(
            "Bearer $token",
            userId, FirebaseTokenUpdateRequest(token)
        )
    }
}