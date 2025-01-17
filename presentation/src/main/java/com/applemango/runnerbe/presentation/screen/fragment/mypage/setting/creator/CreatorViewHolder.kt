package com.applemango.runnerbe.presentation.screen.fragment.mypage.setting.creator

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemCreatorBinding
import com.applemango.runnerbe.presentation.model.type.CreatorImageAndPosition

class CreatorViewHolder(val binding : ItemCreatorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : CreatorImageAndPosition) {
        binding.data = item
    }
}