package com.applemango.presentation.ui.screen.fragment.chat

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.RunningTalkRoomModel
import com.applemango.presentation.ui.model.listener.RoomClickListener
import com.applemango.presentation.databinding.ItemRunningTalkBinding

class RunningTalkViewHolder(
    val binding: ItemRunningTalkBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RunningTalkRoomModel, listener: RoomClickListener) {
        binding.item = item
        binding.clickListener = listener
        binding.executePendingBindings()
    }
}