package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemGotStampBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.bumptech.glide.Glide

class GotStampAdapter : ListAdapter<MemberStampData, GotStampAdapter.GotStampViewHolder>(
    GOT_STAMP_DIFF_UTIL
) {

    class GotStampViewHolder(
        private val binding: ItemGotStampBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MemberStampData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.profileImageUrl)
                    .error(R.drawable.ic_user_default)
                    .into(ivPhoto)
                Glide.with(itemView.context)
                    .load(getStampItemByCode(item.stampCode))
                    .error(R.drawable.ic_user_default)
                    .into(ivPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GotStampViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GotStampViewHolder(ItemGotStampBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: GotStampViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val GOT_STAMP_DIFF_UTIL = object: DiffUtil.ItemCallback<MemberStampData>() {
            override fun areItemsTheSame(oldItem: MemberStampData, newItem: MemberStampData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MemberStampData, newItem: MemberStampData): Boolean {
                return oldItem.nickname == newItem.nickname
            }

        }
    }
}