package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.monthly

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemDateMonthlyBinding
import com.applemango.runnerbe.databinding.ItemDateMonthlyEmptyBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.DateItem
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.OnDateClickListener
import java.time.LocalDate

class MonthlyEmptyViewHolder (
    val binding: ItemDateMonthlyEmptyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem) {
        val date = item.date
        with(binding) {
            if (date != null) {
                tvDate.text = date.dayOfMonth.toString()
            } else {
                tvDate.text = ""
            }
        }
    }
}

class MonthlyOtherUserDateViewHolder (
    private val binding: ItemDateMonthlyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, onDateClickListener: OnDateClickListener) {
        with(binding) {
            item.date?.let {
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
                ivStamp.setImageResource(getStampItemByCode(log.stampCode).image)
                llDate.setOnClickListener {
                    onDateClickListener.onDateClicked(item)
                }
            } ?: ivStamp.setImageResource(StampItem.otherUserUnavailableStampItem.image)
        }
    }
}

class MonthlyDateViewHolder (
    private val binding: ItemDateMonthlyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, onDateClickListener: OnDateClickListener) {
        with(binding) {
            item.date?.let {
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
                onDateClickListener.onDateClicked(item)
            }
        }
    }
}