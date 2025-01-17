package com.applemango.runnerbe.presentation.model.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class RunningTalkFcmService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    private fun sendTokenToServer(token: String){
        //TODO
        //여기에 서버로 토큰 보내기
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //TODO
        //여기에서 알림 처리
    }
}