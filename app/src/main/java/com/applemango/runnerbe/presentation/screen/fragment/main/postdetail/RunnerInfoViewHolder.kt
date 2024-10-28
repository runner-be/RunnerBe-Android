package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.databinding.ItemRunnerInfoBinding

class RunnerInfoViewHolder(val binding: ItemRunnerInfoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserInfo, runnerInfoClickListener: RunnerInfoClickListener) {
        binding.userInfo = item
        binding.listener = runnerInfoClickListener
    }
}