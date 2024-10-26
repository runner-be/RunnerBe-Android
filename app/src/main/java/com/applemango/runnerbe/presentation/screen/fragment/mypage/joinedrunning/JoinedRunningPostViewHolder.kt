package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding

class JoinedRunningPostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Posting, listener: JoinedRunningClickListener, from: PostCalledFrom) {
        binding.item = item
        binding.clickListener = listener
        binding.postFrom = from
    }
}