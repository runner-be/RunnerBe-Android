package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding

class JoinedRunningPostAdapter : ListAdapter<Posting, JoinedRunningPostViewHolder>(
    joinedRunningPostDiffUtil
) {
    private lateinit var listener: JoinedRunningClickListener
    private lateinit var postFrom: PostCalledFrom

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinedRunningPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return JoinedRunningPostViewHolder(ItemJoinPostWithBookmarkBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: JoinedRunningPostViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, listener, postFrom)
        }
    }

    fun setPostClickListener(listener: JoinedRunningClickListener) {
        this.listener = listener
    }

    fun setPostFrom(from: PostCalledFrom) {
        this.postFrom = from
    }

    companion object {
        private val joinedRunningPostDiffUtil = object : DiffUtil.ItemCallback<Posting>() {
            override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem == newItem
            }
        }
    }
}