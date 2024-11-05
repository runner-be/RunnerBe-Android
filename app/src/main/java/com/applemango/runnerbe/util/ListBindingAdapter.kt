package com.applemango.runnerbe.util

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.presentation.model.CreatorImageAndPosition
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.presentation.model.listener.*
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectListData
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectListItemAdapter
import com.applemango.runnerbe.presentation.screen.fragment.map.write.paceselect.PaceCheckSelectListAdapter
import com.applemango.runnerbe.presentation.screen.fragment.map.write.paceselect.PaceSimpleSelectListAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession.AttendanceAccessionAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.see.AttendanceSeeAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.PaceInfoListAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.PaceSelectItem
import com.applemango.runnerbe.presentation.screen.fragment.mypage.setting.creator.CreatorAdapter

@BindingAdapter("attendanceSeeAdapter")
fun setAttendanceSeeAdapter(
    recyclerView: RecyclerView,
    dataList: ObservableArrayList<UserInfo>
) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = AttendanceSeeAdapter(dataList)
    }
    recyclerView.adapter?.notifyDataSetChanged()
}

@BindingAdapter("attendanceAccessionAdapter", "attendanceAccessionClickListener")
fun setAttendanceAccessionAdapter(
    recyclerView: RecyclerView,
    dataList: ObservableArrayList<UserInfo>,
    accessionClickListener: AttendanceAccessionClickListener
) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = AttendanceAccessionAdapter(dataList, accessionClickListener)
    }
    recyclerView.adapter?.notifyDataSetChanged()
}

@BindingAdapter("creatorAdapter")
fun setCreatorAdapter(recyclerView: RecyclerView, dataList: List<CreatorImageAndPosition>) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = CreatorAdapter(dataList)
    }
    recyclerView.adapter?.notifyDataSetChanged()
}

@BindingAdapter("selectListAdapter")
fun setSelectListAdapter(
    recyclerView: RecyclerView,
    dataList: ObservableArrayList<SelectListData>
) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = SelectListItemAdapter(dataList)
    }
    recyclerView.adapter?.notifyDataSetChanged()
}

@BindingAdapter("bind:paceListAdapter", "bind:paceSelectListener")
fun setPaceListAdapter(
    recyclerView: RecyclerView,
    dataList: List<PaceSelectItem>,
    listener: PaceSelectListener
) {
    recyclerView.adapter ?: run {
        recyclerView.adapter = PaceInfoListAdapter(listener)
    }
    val adapter = recyclerView.adapter
    recyclerView.itemAnimator = null
    if (adapter is PaceInfoListAdapter) adapter.submitList(dataList)
}

@BindingAdapter("bind:paceSimpleListAdapter", "bind:paceSimpleSelectListener")
fun setPaceSimpleSelectAdapter(
    recyclerView: RecyclerView,
    dataList: List<PaceSelectItem>,
    listener: PaceSelectListener
) {
    recyclerView.adapter ?: run {
        recyclerView.adapter = PaceSimpleSelectListAdapter(listener)
    }
    val adapter = recyclerView.adapter
    recyclerView.itemAnimator = null
    if (adapter is PaceSimpleSelectListAdapter) adapter.submitList(dataList)
}

@BindingAdapter("bind:paceCheckListAdapter", "bind:paceCheckSelectListener")
fun setPaceCheckSelectAdapter(
    recyclerView: RecyclerView,
    dataList: List<PaceSelectItem>,
    listener: PaceSelectListener
) {
    recyclerView.adapter ?: run {
        recyclerView.adapter = PaceCheckSelectListAdapter(listener)
    }
    val adapter = recyclerView.adapter
    recyclerView.itemAnimator = null
    if (adapter is PaceCheckSelectListAdapter) adapter.submitList(dataList)
}