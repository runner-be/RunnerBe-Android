package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting

class JoinedPostAdapter : ListAdapter<Posting, JoinedPostViewHolder>(joinedPostDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinedPostViewHolder {
        return JoinedPostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_join_post,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JoinedPostViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
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