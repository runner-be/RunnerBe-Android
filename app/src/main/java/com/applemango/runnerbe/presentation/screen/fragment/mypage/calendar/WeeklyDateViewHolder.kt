package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemDateWeeklyBinding
import com.applemango.runnerbe.databinding.ItemDateWeeklyEmptyBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
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

class WeeklyDateViewHolder (
    val binding: ItemDateWeeklyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, listener: OnDateClickListener) {
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
            item.runningLog?.let {
                ivStamp.setImageResource(getStampItemByCode(it.stampCode).image)
            } ?: ivStamp.setImageResource(StampItem.unavailableStampItem.image)
            llDate.setOnClickListener {
                listener.onDateClicked(item)
//                    if (item.stampItem != StampItem.unavailableStampItem) {
//                        listener.onDateClicked(item)
//                    }
            }
        }
    }
}