package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applemango.runnerbe.databinding.ItemWaitingRunnerInfoBinding
import com.applemango.runnerbe.presentation.model.UserModel

class WaitingRunnerInfoViewHolder(
    val binding: ItemWaitingRunnerInfoBinding,
) : ViewHolder(binding.root) {

    fun bind(user: UserModel, listener: WaitingRunnerClickListener) {
        binding.user = user
        binding.listener = listener
    }
}