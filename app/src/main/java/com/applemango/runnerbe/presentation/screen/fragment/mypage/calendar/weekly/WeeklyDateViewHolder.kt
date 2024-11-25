package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.weekly

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemDateWeeklyBinding
import com.applemango.runnerbe.databinding.ItemDateWeeklyEmptyBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.DateItem
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.OnDateClickListener
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class WeeklyEmptyViewHolder (
    val binding: ItemDateWeeklyEmptyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem) {
        with(binding) {
            item.date?.let {
                tvDayOfWeek.text = it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())[0].toString()
                tvDate.text = it.dayOfMonth.toString()
            }
        }
    }
}

class WeeklyOtherUserDateViewHolder (
    val binding: ItemDateWeeklyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, listener: OnDateClickListener?) {
        with(binding) {
            item.date?.let {
                tvDayOfWeek.text = it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())[0].toString()
                tvDate.apply {
                    text = it.dayOfMonth.toString()
                    if (it == LocalDate.now()) {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g2, null))
                    } else {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g5, null))
                    }
                }
            }
            item.runningLog?.let { log ->
                // 다른 사람 로그 목록에서는 그룹 로그 작성이 가능해도 변경 불가능 이미지로 표기
                val stampImage = if (log.stampCode == null || log.stampCode == StampItem.availableStampItem.code) {
                    StampItem.otherUserUnavailableStampItem.image
                } else {
                    getStampItemByCode(log.stampCode).image
                }
                ivStamp.setImageResource(stampImage)
                llDate.setOnClickListener {
                    if (item.runningLog.isOpened == 1) {
                        listener?.onDateClicked(item)
                    }
                }
            } ?: ivStamp.setImageResource(StampItem.otherUserUnavailableStampItem.image)
        }
    }
}

class WeeklyDateViewHolder (
    val binding: ItemDateWeeklyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, listener: OnDateClickListener?) {
        with(binding) {
            item.date?.let {
                tvDayOfWeek.text = it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())[0].toString()
                tvDate.apply {
                    text = it.dayOfMonth.toString()
                    if (it == LocalDate.now()) {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g2, null))
                    } else {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g5, null))
                    }
                }
            }
            item.runningLog?.let { log ->
                if (log.stampCode == null && log.gatheringId != null) {
                    ivStamp.setImageResource(StampItem.availableStampItem.image)
                } else {
                    ivStamp.setImageResource(getStampItemByCode(log.stampCode).image)
                }
            } ?: ivStamp.setImageResource(StampItem.unavailableStampItem.image)
            llDate.setOnClickListener {
                listener?.onDateClicked(item)
            }
        }
    }
}