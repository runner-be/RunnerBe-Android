package com.applemango.presentation.ui.screen.fragment.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.type.PostCalledFrom
import com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.presentation.R
import javax.inject.Inject

class PostAdapter @Inject constructor(): ListAdapter<PostingModel, PostViewHolder>(postDiffUtil) {
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
        private val postDiffUtil = object : DiffUtil.ItemCallback<PostingModel>() {
            override fun areItemsTheSame(oldItem: PostingModel, newItem: PostingModel): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: PostingModel, newItem: PostingModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}