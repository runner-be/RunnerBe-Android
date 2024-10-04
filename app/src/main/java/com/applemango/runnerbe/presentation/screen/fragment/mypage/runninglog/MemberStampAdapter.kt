package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemMemberStampBinding

class MemberStampAdapter : ListAdapter<MemberStampData, RecyclerView.ViewHolder>(MEMBER_STAMP_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MemberStampViewHolder(ItemMemberStampBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as MemberStampViewHolder).bind(item)
    }

    companion object {
        private val MEMBER_STAMP_DIFF_UTIL = object : DiffUtil.ItemCallback<MemberStampData>() {
            override fun areItemsTheSame(
                oldItem: MemberStampData,
                newItem: MemberStampData
            ): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(
                oldItem: MemberStampData,
                newItem: MemberStampData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}