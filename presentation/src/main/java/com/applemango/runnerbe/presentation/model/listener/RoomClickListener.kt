package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.presentation.model.RunningTalkRoomModel

interface RoomClickListener {
    fun moveToRunningTalkRoom(item : RunningTalkRoomModel)
}