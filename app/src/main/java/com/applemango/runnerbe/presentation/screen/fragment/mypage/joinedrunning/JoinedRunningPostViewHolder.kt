package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemJoinPostWithBookmarkBinding
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.model.type.PostCalledFrom
import java.time.ZoneId
import java.time.ZonedDateTime

class JoinedRunningPostViewHolder(
    private val binding: ItemJoinPostWithBookmarkBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PostingModel, listener: JoinedRunningClickListener, from: PostCalledFrom) {
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

            val secondButtonVisibility =
                isRunningEnd && !isThreeHourAfterRunningEnd && item.isRunningCaptain()
            val thirdButtonVisibility = isRunningEnd && isThreeHourAfterRunningEnd
            val firstButtonVisibility = !(secondButtonVisibility || thirdButtonVisibility)

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

        val threeHoursAfterRunningEnd = runningEndTime.plusHours(3)
        val currentTimeKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val currentTimeUTC = formatToUTC(currentTimeKST).plusHours(9)

        return currentTimeUTC.isAfter(threeHoursAfterRunningEnd)
    }

    private fun isRunningEnd(
        gatheringTime: ZonedDateTime,
        runningTime: String
    ): Boolean {
        val runningTimeParts = runningTime.split(":").map { it.toInt() }
        val runningDurationMinutes = runningTimeParts[0] * 60 + runningTimeParts[1]

        val runningEndTime = gatheringTime.plusMinutes(runningDurationMinutes.toLong())

        val currentTimeKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val currentTimeUTC = formatToUTC(currentTimeKST).plusHours(9)

        return currentTimeUTC.isAfter(runningEndTime)
    }

    private fun formatToUTC(zonedDateTime: ZonedDateTime): ZonedDateTime {
        return zonedDateTime
            .withZoneSameInstant(java.time.ZoneOffset.UTC)
            .withSecond(0)
            .withNano(0)
    }
}