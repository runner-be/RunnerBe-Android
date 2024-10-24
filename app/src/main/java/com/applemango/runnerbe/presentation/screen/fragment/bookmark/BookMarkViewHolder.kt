package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.util.LogUtil

class BookMarkViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting, listener: JoinedRunningClickListener) {
        LogUtil.errorLog("itemId: ${item.postId} is bookmarked? = ${item.bookmarkCheck()}")
        binding.item = item
        binding.clickListener = listener
    }
}