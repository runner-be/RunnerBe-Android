package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.R

class OtherUserJoinedPostAdapter : ListAdapter<PostingModel, OtherUserJoinedPostViewHolder>(
    joinedPostDiffUtil
) {
    private lateinit var onPostClickListener: OtherUserJoinedPostClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherUserJoinedPostViewHolder {
        return OtherUserJoinedPostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_join_post_without_bookmark,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OtherUserJoinedPostViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onPostClickListener)
        }
    }

    fun setOnPostClickListener(listener: OtherUserJoinedPostClickListener) {
        this.onPostClickListener = listener
    }

    companion object {
        private val joinedPostDiffUtil = object : DiffUtil.ItemCallback<PostingModel>() {
            override fun areItemsTheSame(oldItem: PostingModel, newItem: PostingModel): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: PostingModel, newItem: PostingModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}