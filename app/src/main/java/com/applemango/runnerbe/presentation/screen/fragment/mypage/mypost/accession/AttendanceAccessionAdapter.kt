package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.presentation.model.listener.AttendanceAccessionClickListener
import java.lang.IllegalArgumentException

class AttendanceAccessionAdapter: ListAdapter<UserInfo, AttendanceAccessionViewHolder>(attendanceSeeDiffUtil) {
    private var accessionClickListener: AttendanceAccessionClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceAccessionViewHolder {
        return AttendanceAccessionViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_attendance_accession,
                parent,
                false
            ), accessionClickListener ?: throw IllegalArgumentException("AttendanceAccessionClickListener is NULL")
        )
    }

    override fun onBindViewHolder(holder: AttendanceAccessionViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    fun setAccessionClickListener(listener: AttendanceAccessionClickListener) {
        this.accessionClickListener = listener
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