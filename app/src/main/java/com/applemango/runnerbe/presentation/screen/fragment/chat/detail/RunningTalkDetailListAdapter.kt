package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemMyTalkContainerBinding
import com.applemango.runnerbe.databinding.ItemOtherTalkContainerBinding
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState

class RunningTalkDetailListAdapter(val listener: RunningTalkDetailListClickListener) :
    ListAdapter<RunningTalkUiState, RecyclerView.ViewHolder>(RunningTalkDetailDiffCallBack()) {

    private val myViewType = 1
    private val otherViewType = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            myViewType -> {
                RunningTalkDetailMyContainerViewHolder(
                    ItemMyTalkContainerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), listener = listener
                )
            }

            else -> {
                RunningTalkDetailOtherContainerViewHolder(
                    ItemOtherTalkContainerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), listener = listener
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
            is RunningTalkUiState.MyRunningTalkUiState -> myViewType
            is RunningTalkUiState.OtherRunningTalkUiState -> otherViewType
        }
    }
}

class RunningTalkDetailDiffCallBack : DiffUtil.ItemCallback<RunningTalkUiState>() {
    override fun areItemsTheSame(
        oldItem: RunningTalkUiState,
        newItem: RunningTalkUiState
    ): Boolean = oldItem == newItem


    override fun areContentsTheSame(
        oldItem: RunningTalkUiState,
        newItem: RunningTalkUiState
    ): Boolean {
        return when (oldItem) {
            is RunningTalkUiState.MyRunningTalkUiState -> {
                if (newItem is RunningTalkUiState.MyRunningTalkUiState) {
                    oldItem == newItem
                } else false
            }

            is RunningTalkUiState.OtherRunningTalkUiState -> {
                if (newItem is RunningTalkUiState.OtherRunningTalkUiState) {
                    oldItem == newItem
                } else false
            }
        }
    }
}

interface RunningTalkDetailListClickListener {
    fun imageClicked(imageUrl: String, talkIdList: List<Int>, clickItemId: Int)
}