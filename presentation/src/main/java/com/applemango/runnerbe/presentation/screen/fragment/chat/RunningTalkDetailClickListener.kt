package com.applemango.runnerbe.presentation.screen.fragment.chat

interface RunningTalkDetailClickListener {
    fun imageClicked(imageUrl: String, talkIdList: List<Int>, clickItemId: Int)
}