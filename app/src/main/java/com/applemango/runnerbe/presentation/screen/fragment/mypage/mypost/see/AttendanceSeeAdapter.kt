package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.see

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo

class AttendanceSeeAdapter() : ListAdapter<UserInfo, AttendanceSeeViewHolder>(attendanceSeeDiffUtil) {
    private var listener: AttendanceProfileClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceSeeViewHolder {
        return AttendanceSeeViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_attendance_see,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: AttendanceSeeViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, listener ?: throw IllegalStateException("AttendanceProfileClickListener not initialized"))
    }

    fun initProfileClickListener(listener: AttendanceProfileClickListener) {
        this.listener = listener
    }

    companion object {
        private val attendanceSeeDiffUtil = object : DiffUtil.ItemCallback<UserInfo>() {
            override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem.userId == newItem.userId
            }

        }
    }
}