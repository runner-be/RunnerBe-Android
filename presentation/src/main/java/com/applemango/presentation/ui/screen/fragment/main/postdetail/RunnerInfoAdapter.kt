package com.applemango.presentation.ui.screen.fragment.main.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.R

class RunnerInfoAdapter : ListAdapter<UserModel, RunnerInfoViewHolder>(runnerInfoDiffUtil){
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
        private val runnerInfoDiffUtil = object: DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}