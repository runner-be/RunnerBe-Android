package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import com.applemango.runnerbe.data.network.response.OtherUserPosting

fun interface OtherUserJoinedPostClickListener {
    fun onPostClick(item: OtherUserPosting)
}