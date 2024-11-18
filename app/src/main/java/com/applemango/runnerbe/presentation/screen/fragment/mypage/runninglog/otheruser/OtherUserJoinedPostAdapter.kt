package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.OtherUserPosting

class OtherUserJoinedPostAdapter : ListAdapter<Posting, OtherUserJoinedPostViewHolder>(
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
        private val joinedPostDiffUtil = object : DiffUtil.ItemCallback<Posting>() {
            override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem == newItem
            }
        }
    }
}