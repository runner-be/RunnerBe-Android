package com.applemango.runnerbe.presentation.screen.fragment.map

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.PostCalledFrom

class PostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding,
    private val listener: JoinedRunningClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting, from: PostCalledFrom) {
        binding.item = item
        binding.clickListener = listener
        binding.postFrom = from
    }
}