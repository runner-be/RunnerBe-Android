package com.applemango.domain.repository

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.entity.RunningTalkMessagesEntity
import com.applemango.domain.entity.RunningTalkRoomEntity

interface RunningTalkRepository {
    suspend fun getRunningTalkRooms() : List<RunningTalkRoomEntity>
    suspend fun getRunningTalkMessages(roomId: Int): RunningTalkMessagesEntity
    suspend fun sendMessage(roomId: Int, content: String?, url: String?) : CommonEntity
    suspend fun reportMessage(messageIdList : List<Int>) : CommonEntity
}