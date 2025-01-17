package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemJoinPostWithoutBookmarkBinding
import com.applemango.runnerbe.presentation.model.PostingModel

class OtherUserJoinedPostViewHolder(
    val binding : ItemJoinPostWithoutBookmarkBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PostingModel, listener: OtherUserJoinedPostClickListener) {
        binding.item = item
        binding.clickListener = listener
    }
}