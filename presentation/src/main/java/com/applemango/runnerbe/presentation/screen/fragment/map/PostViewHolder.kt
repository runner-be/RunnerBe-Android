package com.applemango.runnerbe.presentation.screen.fragment.map

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.presentation.model.type.PostCalledFrom

class PostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding,
    private val listener: JoinedRunningClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PostingModel, from: PostCalledFrom) {
        binding.item = item
        binding.clickListener = listener
        binding.postFrom = from
    }
}