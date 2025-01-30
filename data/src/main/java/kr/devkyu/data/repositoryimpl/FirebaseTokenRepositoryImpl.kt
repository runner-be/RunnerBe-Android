package kr.devkyu.data.repositoryimpl

import com.applemango.runnerbe.repository.FirebaseTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.devkyu.data.network.UserDataStore
import kr.devkyu.data.network.api.UpdateFirebaseTokenApi
import kr.devkyu.data.network.request.FirebaseTokenUpdateRequest
import javax.inject.Inject

class FirebaseTokenRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore,
    private val updateFirebaseTokenApi: UpdateFirebaseTokenApi
): FirebaseTokenRepository {

    override suspend fun updateFirebaseToken() {
        val userId = userDataStore.getUserId().first()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener

            val token = task.result
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    userDataStore.setDeviceToken(token)
                    updateFirebaseTokenApi.firebaseTokenUpdate(
                        "Bearer $token",
                        userId,
                        FirebaseTokenUpdateRequest(token)
                    )
                }
            }
        }
    }
}