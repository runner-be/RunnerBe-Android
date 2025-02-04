package com.applemango.presentation.ui.screen.fragment.mypage.mypost.accession

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.ui.model.listener.AttendanceAccessionClickListener
import com.applemango.presentation.R
import java.lang.IllegalArgumentException

class AttendanceAccessionAdapter: ListAdapter<UserModel, AttendanceAccessionViewHolder>(attendanceSeeDiffUtil) {
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
        private val attendanceSeeDiffUtil = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem.userId == newItem.userId
            }

        }
    }
}