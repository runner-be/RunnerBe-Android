package com.applemango.runnerbe.presentation.screen.fragment.chat

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemRunningTalkBinding
import com.applemango.runnerbe.presentation.model.listener.RoomClickListener
import com.applemango.runnerbe.presentation.model.RunningTalkRoomModel

class RunningTalkViewHolder(
    val binding: ItemRunningTalkBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RunningTalkRoomModel, listener: RoomClickListener) {
        binding.item = item
        binding.clickListener = listener
        binding.executePendingBindings()
    }
}