package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.presentation.model.listener.PostAcceptListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener

class WaitingRunnerInfoAdapter : ListAdapter<UserInfo, WaitingRunnerInfoViewHolder>(waitingRunnerDiffUtil) {
    private lateinit var listener: WaitingRunnerClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaitingRunnerInfoViewHolder {
        return WaitingRunnerInfoViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_waiting_runner_info,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WaitingRunnerInfoViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, listener)
        }
    }

    fun setProfileClickListener(listener: WaitingRunnerClickListener) {
        this.listener = listener
    }

    companion object {
        private val waitingRunnerDiffUtil = object : DiffUtil.ItemCallback<UserInfo>() {
            override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem == newItem
            }

        }
    }
}