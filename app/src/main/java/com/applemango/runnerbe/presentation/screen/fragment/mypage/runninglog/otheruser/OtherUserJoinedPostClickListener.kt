package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import com.applemango.runnerbe.data.dto.Posting

fun interface OtherUserJoinedPostClickListener {
    fun onPostClick(item: Posting)
}