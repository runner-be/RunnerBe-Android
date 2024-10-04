package com.applemango.runnerbe.presentation.screen.fragment.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemPostBinding

class JoinedRunningPostAdapter : ListAdapter<Posting, JoinedRunningPostViewHolder>(
    joinedRunningPostDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinedRunningPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return JoinedRunningPostViewHolder(ItemPostBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: JoinedRunningPostViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
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