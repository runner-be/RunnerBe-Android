package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.see

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.databinding.ItemAttendanceSeeBinding

class AttendanceSeeViewHolder(private val binding : ItemAttendanceSeeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserInfo) {
        binding.userInfo = item
        binding.vh = this
    }

    fun getAttendanceMessageResource(userInfo: UserInfo) : Int {
        return if(userInfo.whetherCheck == "Y") {
            if(userInfo.attendance == 1) R.string.msg_attendance_complete
            else R.string.msg_not_attendance_complete
        } else R.string.msg_not_attendance_check
    }

    fun isAttendanceComplete(userInfo: UserInfo) : Boolean {
        return userInfo.whetherCheck == "Y" && userInfo.attendance == 1
    }
}