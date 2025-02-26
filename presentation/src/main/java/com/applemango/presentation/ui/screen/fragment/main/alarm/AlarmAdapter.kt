package com.applemango.presentation.ui.screen.fragment.main.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.presentation.ui.model.AlarmModel
import com.applemango.presentation.databinding.ItemAlarmBinding

class AlarmAdapter: ListAdapter<AlarmModel, AlarmViewHolder>(alarmDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlarmViewHolder(ItemAlarmBinding.inflate(inflater, parent,false))
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    companion object {
        private val alarmDiffUtil = object : DiffUtil.ItemCallback<AlarmModel>() {
            override fun areItemsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
                return oldItem== newItem
            }

            override fun areContentsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}