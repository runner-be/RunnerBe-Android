package com.applemango.runnerbe.presentation.screen.fragment.main.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.data.network.response.Alarm
import com.applemango.runnerbe.databinding.ItemAlarmBinding

class AlarmAdapter: ListAdapter<Alarm, AlarmViewHolder>(alarmDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlarmViewHolder(ItemAlarmBinding.inflate(inflater, parent,false))
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    companion object {
        private val alarmDiffUtil = object : DiffUtil.ItemCallback<Alarm>() {
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem== newItem
            }

            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem == newItem
            }
        }
    }
}