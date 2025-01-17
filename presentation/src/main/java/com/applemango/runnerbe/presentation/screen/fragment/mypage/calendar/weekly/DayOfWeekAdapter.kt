package com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.weekly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemDayOfWeekBinding

class DayOfWeekAdapter: ListAdapter<String, DayOfWeekAdapter.DayOfWeekViewHolder>(dayOfWeekDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayOfWeekViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DayOfWeekViewHolder(ItemDayOfWeekBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: DayOfWeekViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class DayOfWeekViewHolder(
        private val binding: ItemDayOfWeekBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvDayOfWeek.text = item
        }
    }

    companion object {
        private const val DAY_OF_WEEK_SIZE = 7
        private val dayOfWeekDiffUtil = object: DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}