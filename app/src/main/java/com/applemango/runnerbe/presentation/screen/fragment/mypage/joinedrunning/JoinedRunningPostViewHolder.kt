package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import java.lang.IllegalArgumentException
import java.time.ZonedDateTime

class JoinedRunningPostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Posting, listener: JoinedRunningClickListener, from: PostCalledFrom) {
        with(binding) {
            this.item = item
            clickListener = listener
            postFrom = from

            val isRunningEnd = try {
                val gatheringTime = requireNotNull(item.gatheringTime)
                val runningTime = requireNotNull(item.runningTime)
                isRunningEnd(gatheringTime, runningTime)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                false
            }

            val isThreeHourAfterRunningEnd = try {
                val gatheringTime = requireNotNull(item.gatheringTime)
                val runningTime = requireNotNull(item.runningTime)
                isThreeHourAfterRunningEnd(gatheringTime, runningTime)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                false
            }

            val firstButtonVisibility = !isRunningEnd
            val secondButtonVisibility = isRunningEnd && item.isRunningCaptain() && !isThreeHourAfterRunningEnd
            val thirdButtonVisibility = isRunningEnd && isThreeHourAfterRunningEnd

            runningEndTextViewVisibility = firstButtonVisibility
            attendanceCheckButtonVisibility = secondButtonVisibility
            attendanceSeeButtonVisibility = thirdButtonVisibility
        }
    }

    private fun isThreeHourAfterRunningEnd(
        gatheringTime: ZonedDateTime,
        runningTime: String
    ): Boolean {
        val runningTimeParts = runningTime.split(":").map { it.toInt() }
        val runningDurationMinutes = runningTimeParts[0] * 60 + runningTimeParts[1]
        val runningEndTime = gatheringTime.plusMinutes(runningDurationMinutes.toLong())
        val currentTime = ZonedDateTime.now()
         return currentTime.isAfter(runningEndTime.plusMinutes(180))
    }

    private fun isRunningEnd(
        gatheringTime: ZonedDateTime,
        runningTime: String
    ): Boolean {
        val runningTimeParts = runningTime.split(":").map { it.toInt() }
        val runningDurationMinutes = runningTimeParts[0] * 60 + runningTimeParts[1]
        val runningEndTime = gatheringTime.plusMinutes(runningDurationMinutes.toLong())
        val currentTime = ZonedDateTime.now()
        return currentTime.isAfter(runningEndTime)
    }
}