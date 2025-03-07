package com.applemango.presentation.util

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.listener.PaceSelectListener
import com.applemango.presentation.ui.screen.dialog.selectitem.SelectListData
import com.applemango.presentation.ui.screen.dialog.selectitem.SelectListItemAdapter
import com.applemango.presentation.ui.screen.fragment.map.write.paceselect.PaceCheckSelectListAdapter
import com.applemango.presentation.ui.screen.fragment.map.write.paceselect.PaceSimpleSelectListAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.paceinfo.PaceInfoListAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.paceinfo.PaceSelectItem

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