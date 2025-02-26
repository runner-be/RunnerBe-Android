package com.applemango.presentation.ui.screen.fragment.bookmark

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.presentation.databinding.ItemJoinPostWithBookmarkBinding

class BookMarkViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PostingModel, listener: JoinedRunningClickListener) {
        binding.item = item
        binding.clickListener = listener
    }
}