package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.util.LogUtil

class JoinedRunningPostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Posting, listener: JoinedRunningClickListener) {
        LogUtil.errorLog(item.toString())
        binding.item = item
        binding.clickListener = listener
    }
}