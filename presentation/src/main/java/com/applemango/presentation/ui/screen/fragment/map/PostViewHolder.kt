package com.applemango.presentation.ui.screen.fragment.map

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.type.PostCalledFrom
import com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.presentation.databinding.ItemJoinPostWithBookmarkBinding

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