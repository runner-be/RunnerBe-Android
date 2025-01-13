package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.RunningTalkMessagesEntity
import com.applemango.runnerbe.entity.RunningTalkRoomsEntity

interface RunningTalkRepository {
    suspend fun getRunningTalkRooms() : RunningTalkRoomsEntity
    suspend fun getRunningTalkMessages(roomId: Int): RunningTalkMessagesEntity
    suspend fun sendMessage(roomId: Int, content: String?, url: String?) : CommonEntity
    suspend fun reportMessage(messageIdList : List<Int>) : CommonEntity
}