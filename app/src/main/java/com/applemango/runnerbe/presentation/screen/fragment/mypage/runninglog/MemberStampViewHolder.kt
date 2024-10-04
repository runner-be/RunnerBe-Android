package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemMemberStampBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.bumptech.glide.Glide

class MemberStampViewHolder(
    private val binding: ItemMemberStampBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MemberStampData) {
        with(binding) {
            val profileImage = item.profileImageUrl
            val stampImage = getStampItemByCode(item.stampCode)

            Glide.with(itemView.context)
                .load(profileImage)
                .into(ivStamp)
            Glide.with(itemView.context)
                .load(stampImage)
                .into(ivStamp)
        }
    }
}