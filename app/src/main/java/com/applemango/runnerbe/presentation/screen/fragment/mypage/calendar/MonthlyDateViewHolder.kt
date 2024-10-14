package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar

import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemDateMonthlyBinding
import com.applemango.runnerbe.databinding.ItemDateMonthlyEmptyBinding
import com.applemango.runnerbe.databinding.ItemDateWeeklyEmptyBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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

class MonthlyDateViewHolder (
    private val binding: ItemDateMonthlyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, onDateClickListener: OnDateClickListener) {
        Log.e("MonthlyDateViewHolder", item.toString())

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
            item.runningLog?.let {
                ivStamp.setImageResource(getStampItemByCode(it.stampCode).image)
            } ?: ivStamp.setImageResource(StampItem.unavailableStampItem.image)
            llDate.setOnClickListener {
                when (item.runningLog?.gatheringId) {
                    null -> {
                        onDateClickListener.onDateClicked(item)
                    }
                    else -> {

                    }
                }
            }
        }
    }
}