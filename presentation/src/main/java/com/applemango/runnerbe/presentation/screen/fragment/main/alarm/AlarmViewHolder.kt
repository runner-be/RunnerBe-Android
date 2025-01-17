package com.applemango.runnerbe.presentation.screen.fragment.main.alarm

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemAlarmBinding
import com.applemango.runnerbe.presentation.model.AlarmModel
import java.time.Duration
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime

class AlarmViewHolder(
    val binding: ItemAlarmBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AlarmModel) {
        binding.item = item

        val currentTimeKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val currentTimeUTC = formatToUTC(currentTimeKST).plusHours(9)

        val passedDays = Period.between(item.createdAt.toLocalDate(), currentTimeUTC.toLocalDate())
        val timeDifference = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val passedTime = Duration.between(currentTimeUTC, item.createdAt)
            TimeDifference(
                passedTime.toHours(),
                passedTime.toMinutes(),
                passedTime.toSeconds(),
            )
        } else {
            calculateTimeDifference(currentTimeUTC, item.createdAt)
        }

        /**
         * 알림이 생성된지 1년 이상/1달 이상/1일 이상/1시간 이상/1분 이상/1초 이상
         */
        binding.createdTime = when {
            passedDays.years >= 1 -> "${passedDays.years}년 전"
            passedDays.months >= 1 -> "${passedDays.months}달 전"
            passedDays.days >= 1 -> "${passedDays.days}일 전"
            timeDifference.hours >= 1 -> "${timeDifference.hours}시간 전"
            timeDifference.minutes >= 1 -> "${timeDifference.minutes}분 전"
            timeDifference.seconds >= 1 -> "${timeDifference.seconds}초 전"
            else -> ""
        }
    }

    private fun calculateTimeDifference(current: ZonedDateTime, createdAt: ZonedDateTime) : TimeDifference {
        val passedTime = Duration.between(createdAt, current)
        return TimeDifference(
            passedTime.toHours(),
            passedTime.toMinutes() % 60,
            passedTime.seconds % 60,
        )
    }

    private fun formatToUTC(zonedDateTime: ZonedDateTime): ZonedDateTime {
        return zonedDateTime
            .withZoneSameInstant(java.time.ZoneOffset.UTC)
            .withSecond(0)
            .withNano(0)
    }

    data class TimeDifference(
        val hours: Long,
        val minutes: Long,
        val seconds: Long,
    )
}