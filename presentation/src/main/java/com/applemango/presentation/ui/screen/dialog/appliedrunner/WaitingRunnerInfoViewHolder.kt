package com.applemango.presentation.ui.screen.dialog.appliedrunner

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applemango.presentation.ui.model.UserModel
import com.applemango.presentation.databinding.ItemWaitingRunnerInfoBinding

class WaitingRunnerInfoViewHolder(
    val binding: ItemWaitingRunnerInfoBinding,
) : ViewHolder(binding.root) {

    fun bind(user: UserModel, listener: WaitingRunnerClickListener) {
        binding.user = user
        binding.listener = listener
    }
}