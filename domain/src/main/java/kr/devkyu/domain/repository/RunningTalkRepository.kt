package kr.devkyu.domain.repository

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.entity.RunningTalkMessagesEntity
import kr.devkyu.domain.entity.RunningTalkRoomsEntity

interface RunningTalkRepository {
    suspend fun getRunningTalkRooms() : RunningTalkRoomsEntity
    suspend fun getRunningTalkMessages(roomId: Int): RunningTalkMessagesEntity
    suspend fun sendMessage(roomId: Int, content: String?, url: String?) : BaseEntity
    suspend fun reportMessage(messageIdList : List<Int>) : BaseEntity
}