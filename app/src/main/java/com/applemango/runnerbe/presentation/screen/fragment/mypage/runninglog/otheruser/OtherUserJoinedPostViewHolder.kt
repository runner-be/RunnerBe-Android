package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.OtherUserPosting
import com.applemango.runnerbe.databinding.ItemJoinPostWithoutBookmarkBinding

class OtherUserJoinedPostViewHolder(val binding : ItemJoinPostWithoutBookmarkBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting, listener: OtherUserJoinedPostClickListener) {
        binding.item = item
        binding.clickListener = listener
    }
}