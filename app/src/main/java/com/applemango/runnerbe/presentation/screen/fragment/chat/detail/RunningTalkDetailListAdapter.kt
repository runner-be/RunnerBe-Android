package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemMyTalkContainerBinding
import com.applemango.runnerbe.databinding.ItemOtherTalkContainerBinding
import com.applemango.runnerbe.presentation.screen.fragment.chat.RunningTalkDetailClickListener
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState

class RunningTalkDetailListAdapter :
    ListAdapter<RunningTalkUiState, RecyclerView.ViewHolder>(talkDetailDiffUtil) {
    private var talkDetailClickListener: RunningTalkDetailClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MY -> {
                RunningTalkDetailMyContainerViewHolder(
                    ItemMyTalkContainerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), talkDetailClickListener!!
                )
            }

            else -> {
                RunningTalkDetailOtherContainerViewHolder(
                    ItemOtherTalkContainerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), talkDetailClickListener!!
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RunningTalkDetailMyContainerViewHolder -> {
                holder.bind(item = getItem(position) as RunningTalkUiState.MyRunningTalkUiState)
            }

            is RunningTalkDetailOtherContainerViewHolder -> {
                holder.bind(item = getItem(position) as RunningTalkUiState.OtherRunningTalkUiState)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RunningTalkUiState.MyRunningTalkUiState -> VIEW_TYPE_MY
            is RunningTalkUiState.OtherRunningTalkUiState -> VIEW_TYPE_OTHER
        }
    }

    fun setTalkDetailClickListener(listener: RunningTalkDetailClickListener) {
        this.talkDetailClickListener = listener
    }

    companion object {
        private const val VIEW_TYPE_MY = 1
        private const val VIEW_TYPE_OTHER = 2
        private val talkDetailDiffUtil = object : DiffUtil.ItemCallback<RunningTalkUiState>() {
            override fun areItemsTheSame(
                oldItem: RunningTalkUiState,
                newItem: RunningTalkUiState
            ): Boolean = when (oldItem) {
                is RunningTalkUiState.MyRunningTalkUiState -> {
                    if (newItem is RunningTalkUiState.MyRunningTalkUiState) {
                        oldItem.items == newItem.items
                    } else false
                }

                is RunningTalkUiState.OtherRunningTalkUiState -> {
                    if (newItem is RunningTalkUiState.OtherRunningTalkUiState) {
                        oldItem.items == newItem.items
                    } else false
                }
            }


            override fun areContentsTheSame(
                oldItem: RunningTalkUiState,
                newItem: RunningTalkUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}