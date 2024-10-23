package com.applemango.runnerbe.presentation.screen.fragment.map

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.presentation.model.PostIncomingType
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener

class PostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding,
    private val listener: JoinedRunningClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting, type: PostIncomingType) {
        binding.item = item
        binding.clickListener = listener
        binding.incomingType = type
    }
}