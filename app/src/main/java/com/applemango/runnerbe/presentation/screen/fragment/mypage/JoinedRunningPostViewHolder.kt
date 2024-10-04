package com.applemango.runnerbe.presentation.screen.fragment.mypage

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemPostBinding

class JoinedRunningPostViewHolder(
    private val binding: ItemPostBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Posting) {
        binding.item = item
    }
}