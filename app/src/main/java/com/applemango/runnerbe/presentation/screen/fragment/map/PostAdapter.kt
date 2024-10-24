package com.applemango.runnerbe.presentation.screen.fragment.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.presentation.model.PostIncomingType
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener

class PostAdapter : ListAdapter<Posting, PostViewHolder>(postDiffUtil) {
    private lateinit var listener: JoinedRunningClickListener
    private lateinit var incomingType: PostIncomingType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_join_post_with_bookmark,
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = currentList[position]
        if (item != null) {
            holder.bind(item, incomingType)
        }
    }

    fun setPostClickListener(listener: JoinedRunningClickListener) {
        this.listener = listener
    }

    fun setIncomingType(incomingType: PostIncomingType) {
        this.incomingType = incomingType
    }

    fun updatePostBookmark(posting: Posting) {
        val item = currentList.first { it.postId == posting.postId }
        val itemIndex = currentList.indexOf(item)
        val bookmarkStatus = if (posting.bookmarkCheck()) 0 else 1
        currentList[itemIndex].bookMark = bookmarkStatus
        notifyItemChanged(itemIndex)
    }

    companion object {
        private val postDiffUtil = object : DiffUtil.ItemCallback<Posting>() {
            override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem == newItem
            }
        }
    }
}