package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemGotStampBinding
import com.applemango.runnerbe.presentation.model.MemberStampModel
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.bumptech.glide.Glide

class GotStampViewHolder(
    private val binding: ItemGotStampBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MemberStampModel, listener : GotStampClickListener?) {
        binding.item = item
        binding.listener = listener
        with(binding) {
            Glide.with(itemView.context)
                .load(getStampItemByCode(item.stampCode)?.image)
                .error(R.drawable.ic_user_default)
                .into(ivStamp)
        }
    }
}