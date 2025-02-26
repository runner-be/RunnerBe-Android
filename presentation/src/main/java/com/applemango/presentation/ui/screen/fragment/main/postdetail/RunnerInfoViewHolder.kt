package com.applemango.presentation.ui.screen.fragment.main.postdetail

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.databinding.ItemRunnerInfoBinding

class RunnerInfoViewHolder(val binding: ItemRunnerInfoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserModel, runnerInfoClickListener: RunnerInfoClickListener) {
        binding.user = item
        binding.listener = runnerInfoClickListener
    }
}