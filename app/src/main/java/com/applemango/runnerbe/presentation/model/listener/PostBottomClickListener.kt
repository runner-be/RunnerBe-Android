package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.data.dto.Posting

interface PostClickListener {

    fun logWriteClick(post: Posting)
    fun attendanceSeeClick(post: Posting)
    fun attendanceManageClick(post: Posting)
    fun bookMarkClick(post: Posting)
    fun postClick(post: Posting)
}