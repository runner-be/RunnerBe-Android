package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.databinding.ItemAttendanceAccessionBinding
import com.applemango.runnerbe.presentation.model.listener.AttendanceAccessionClickListener
import kotlin.properties.Delegates

class AttendanceAccessionViewHolder(
    private val binding: ItemAttendanceAccessionBinding,
    private val accessionClickListener: AttendanceAccessionClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private var attendanceState : Int by Delegates.observable(-1) { property, oldValue, newValue ->
        if(newValue > 0) {
            binding.refuseButton.setBackgroundResource(R.drawable.bg_dark_g4_stroke_radius_20)
            binding.acceptButton.setBackgroundResource(R.drawable.bg_primary_solid_radius_20)
            binding.refuseButton.setTextColor(ResourcesCompat.getColor(RunnerBeApplication.instance.resources, R.color.dark_g3, null))
            binding.acceptButton.setTextColor(ResourcesCompat.getColor(RunnerBeApplication.instance.resources, R.color.black, null))
        } else if(newValue == 0) {
            binding.refuseButton.setBackgroundResource(R.drawable.bg_primary_solid_radius_20)
            binding.acceptButton.setBackgroundResource(R.drawable.bg_dark_g4_stroke_radius_20)
            binding.refuseButton.setTextColor(ResourcesCompat.getColor(RunnerBeApplication.instance.resources, R.color.black, null))
            binding.acceptButton.setTextColor(ResourcesCompat.getColor(RunnerBeApplication.instance.resources, R.color.dark_g3, null))
        } else {
            binding.refuseButton.setBackgroundResource(R.drawable.bg_dark_g4_stroke_radius_20)
            binding.acceptButton.setBackgroundResource(R.drawable.bg_dark_g4_stroke_radius_20)
            binding.refuseButton.setTextColor(ResourcesCompat.getColor(RunnerBeApplication.instance.resources, R.color.dark_g3, null))
            binding.acceptButton.setTextColor(ResourcesCompat.getColor(RunnerBeApplication.instance.resources, R.color.dark_g3, null))
        }
    }

    fun bind(item: UserInfo) {
        binding.userInfo = item
        binding.refuseButton.setOnClickListener {
            item.attendanceState = false
            attendanceState = 0
            accessionClickListener.onRefuseClick(item)
        }
        binding.acceptButton.setOnClickListener{
            item.attendanceState = true
            attendanceState = 1
            accessionClickListener.onAcceptClick(item)

        }
        attendanceState = if(item.whetherCheck != "Y") -1 else item.attendance
    }
}