package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import com.applemango.runnerbe.presentation.model.PostingModel

interface JoinedRunningClickListener {

    fun logWriteClick(post: PostingModel)
    fun attendanceSeeClick(post: PostingModel)
    fun attendanceManageClick(post: PostingModel)
    fun bookMarkClick(post: PostingModel)
    fun postClick(post: PostingModel)
}