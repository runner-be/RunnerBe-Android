package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.databinding.ItemWaitingRunnerInfoBinding
import com.applemango.runnerbe.presentation.model.listener.PostAcceptListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener

class WaitingRunnerInfoViewHolder(
    val binding: ItemWaitingRunnerInfoBinding,
) : ViewHolder(binding.root) {

    fun bind(userInfo: UserInfo, listener: WaitingRunnerClickListener) {
        binding.userInfo = userInfo
        binding.listener = listener
    }
}