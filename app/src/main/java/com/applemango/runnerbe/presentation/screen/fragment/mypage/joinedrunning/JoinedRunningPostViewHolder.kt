package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.util.LogUtil
import java.time.LocalDate
import java.time.ZoneId

class JoinedRunningPostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Posting, listener: JoinedRunningClickListener, from: PostCalledFrom) {
        with(binding) {
            this.item = item
            clickListener = listener
            postFrom = from

            val firstButtonVisibility = (item.whetherEnd == "N") || (item.whetherEnd == "Y" && !item.isRunningCaptain())
            val secondButtonVisibility = item.whetherEnd == "Y" && item.isRunningCaptain()
            val thirdButtonVisibility = item.whetherEnd == "D"

            runningEndTextViewVisibility = firstButtonVisibility
            attendanceCheckButtonVisibility = secondButtonVisibility
            attendanceSeeButtonVisibility = thirdButtonVisibility
        }
    }
}