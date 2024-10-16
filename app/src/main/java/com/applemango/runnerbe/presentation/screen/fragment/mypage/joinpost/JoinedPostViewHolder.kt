package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinpost

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemJoinPostBinding
import com.applemango.runnerbe.data.dto.Posting

class JoinedPostViewHolder(val binding : ItemJoinPostBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Posting) {
        binding.holderModel = item
    }
}