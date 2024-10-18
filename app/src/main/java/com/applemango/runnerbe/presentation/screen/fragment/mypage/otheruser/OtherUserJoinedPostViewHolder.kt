package com.applemango.runnerbe.presentation.screen.fragment.mypage.otheruser

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemJoinPostBinding
import com.applemango.runnerbe.data.dto.Posting

class OtherUserJoinedPostViewHolder(val binding : ItemJoinPostBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting, listener: OtherUserJoinedPostClickListener) {
        binding.item = item
        binding.clickListener = listener
    }
}