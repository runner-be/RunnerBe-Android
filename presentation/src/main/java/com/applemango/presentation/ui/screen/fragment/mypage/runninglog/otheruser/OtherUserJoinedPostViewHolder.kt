package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.databinding.ItemJoinPostWithoutBookmarkBinding

class OtherUserJoinedPostViewHolder(
    val binding : ItemJoinPostWithoutBookmarkBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PostingModel, listener: OtherUserJoinedPostClickListener) {
        binding.item = item
        binding.clickListener = listener
    }
}