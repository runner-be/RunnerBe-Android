package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemRunnerInfoBinding
import com.applemango.runnerbe.presentation.model.UserModel

class RunnerInfoViewHolder(val binding: ItemRunnerInfoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserModel, runnerInfoClickListener: RunnerInfoClickListener) {
        binding.user = item
        binding.listener = runnerInfoClickListener
    }
}