package com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning

import com.applemango.presentation.ui.model.PostingModel

interface JoinedRunningClickListener {

    fun logWriteClick(post: PostingModel)
    fun attendanceSeeClick(post: PostingModel)
    fun attendanceManageClick(post: PostingModel)
    fun bookMarkClick(post: PostingModel)
    fun postClick(post: PostingModel)
}