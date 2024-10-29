package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo

class RunnerInfoAdapter : ListAdapter<UserInfo, RunnerInfoViewHolder>(runnerInfoDiffUtil){
    private lateinit var runnerInfoClickListener: RunnerInfoClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunnerInfoViewHolder {
        return RunnerInfoViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_runner_info,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RunnerInfoViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, runnerInfoClickListener)
        }
    }

    fun setRunnerInfoClickListener(listener: RunnerInfoClickListener) {
        this.runnerInfoClickListener = listener
    }

    companion object {
        private val runnerInfoDiffUtil = object: DiffUtil.ItemCallback<UserInfo>() {
            override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem == newItem
            }

        }
    }
}