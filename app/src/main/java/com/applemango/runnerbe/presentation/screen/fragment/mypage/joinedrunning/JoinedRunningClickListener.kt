package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import com.applemango.runnerbe.data.dto.Posting

interface JoinedRunningClickListener {

    fun logWriteClick(post: Posting)
    fun attendanceSeeClick(post: Posting)
    fun attendanceManageClick(post: Posting)
    fun bookMarkClick(post: Posting)
    fun postClick(post: Posting)
}