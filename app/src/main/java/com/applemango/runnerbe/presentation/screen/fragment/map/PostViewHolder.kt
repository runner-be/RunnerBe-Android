package com.applemango.runnerbe.presentation.screen.fragment.map

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemPostBinding
import com.applemango.runnerbe.presentation.model.PostIncomingType
import com.applemango.runnerbe.presentation.model.listener.PostClickListener

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val listener: PostClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting, type: PostIncomingType) {
        binding.item = item
        binding.clickListener = listener
        binding.incomingType = type
    }
}