package com.applemango.presentation.ui.screen.fragment.mypage.mypost.see

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.R
import com.applemango.presentation.databinding.ItemAttendanceSeeBinding

class AttendanceSeeViewHolder(
    private val binding: ItemAttendanceSeeBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserModel, listener: AttendanceProfileClickListener) {
        binding.userInfo = item
        binding.vh = this
        binding.listener = listener
    }

    fun getAttendanceMessageResource(userInfo: UserModel): Int {
        return if (userInfo.whetherCheck == "Y") {
            if (userInfo.attendance == 1) R.string.msg_attendance_complete
            else R.string.msg_not_attendance_complete
        } else R.string.msg_not_attendance_check
    }

    fun isAttendanceComplete(userInfo: UserModel): Boolean {
        return userInfo.whetherCheck == "Y" && userInfo.attendance == 1
    }
}