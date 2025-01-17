package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.UserModel

class WaitingRunnerInfoAdapter : ListAdapter<UserModel, WaitingRunnerInfoViewHolder>(waitingRunnerDiffUtil) {
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
        private val waitingRunnerDiffUtil = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}