package com.applemango.runnerbe.presentation.screen.fragment.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Room
import com.applemango.runnerbe.presentation.model.listener.RoomClickListener

class RunningTalkAdapter : ListAdapter<Room, RunningTalkViewHolder>(talkDiffUtil) {
    private var roomClickListener: RoomClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningTalkViewHolder {
        return RunningTalkViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_running_talk,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RunningTalkViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            roomClickListener ?: throw IllegalArgumentException("RoomClickListener is NULL")
        )
    }

    fun setRoomClickListener(listener: RoomClickListener) {
        this.roomClickListener = listener
    }

    companion object {
        private val talkDiffUtil = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem.roomId == newItem.roomId
            }

            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem.roomId == newItem.roomId &&
                        oldItem.title == newItem.title &&
                        oldItem.recentMessage == newItem.recentMessage
            }
        }
    }
}