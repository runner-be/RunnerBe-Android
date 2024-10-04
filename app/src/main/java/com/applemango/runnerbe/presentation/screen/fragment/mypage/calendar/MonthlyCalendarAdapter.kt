package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemDateMonthlyBinding
import com.applemango.runnerbe.databinding.ItemDateMonthlyEmptyBinding
import java.time.LocalDate

class MonthlyCalendarAdapter: ListAdapter<DateItem, RecyclerView.ViewHolder>(monthlyDiffUtil) {
    private lateinit var onDateClickListener: OnDateClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_INVALID -> MonthlyEmptyViewHolder(ItemDateMonthlyEmptyBinding.inflate(inflater, parent, false))
            TYPE_VALID -> MonthlyDateViewHolder(ItemDateMonthlyBinding.inflate(inflater, parent, false))
            else -> throw IllegalStateException("${this::class.java.name}: invalid item view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MonthlyEmptyViewHolder -> holder.bind(item)
            is MonthlyDateViewHolder -> holder.bind(item, onDateClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position).date
        return if (item == null || item > LocalDate.now()) {
            WeeklyCalendarAdapter.TYPE_INVALID
        } else {
            WeeklyCalendarAdapter.TYPE_VALID
        }
    }

    fun setOnDateClickListener(onDateClickListener: OnDateClickListener) {
        this.onDateClickListener = onDateClickListener
    }

    companion object {
        const val TYPE_INVALID = 0
        const val TYPE_VALID = 1

        private val monthlyDiffUtil = object : DiffUtil.ItemCallback<DateItem>() {
            override fun areItemsTheSame(
                oldItem: DateItem,
                newItem: DateItem
            ): Boolean {
                return oldItem.runningLog?.runnedDate == newItem.runningLog?.runnedDate
            }

            override fun areContentsTheSame(
                oldItem: DateItem,
                newItem: DateItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
