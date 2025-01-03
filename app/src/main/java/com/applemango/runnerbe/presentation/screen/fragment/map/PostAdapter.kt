package com.applemango.runnerbe.presentation.screen.fragment.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.PostCalledFrom

class PostAdapter : ListAdapter<Posting, PostViewHolder>(postDiffUtil) {
    private lateinit var listener: JoinedRunningClickListener
    private lateinit var postFrom: PostCalledFrom

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
            holder.bind(item, postFrom)
        }
    }

    fun setPostClickListener(listener: JoinedRunningClickListener) {
        this.listener = listener
    }

    fun setPostFrom(from: PostCalledFrom) {
        this.postFrom = from
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