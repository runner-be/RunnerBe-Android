package com.applemango.presentation.ui.screen.fragment.mypage.setting.creator

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.type.CreatorImageAndPosition
import com.applemango.presentation.databinding.ItemCreatorBinding

class CreatorViewHolder(val binding : ItemCreatorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : CreatorImageAndPosition) {
        binding.data = item
    }
}