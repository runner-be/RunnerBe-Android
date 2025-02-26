package com.applemango.presentation.ui.screen.fragment.map.write.paceselect

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.listener.PaceSelectListener
import com.applemango.presentation.ui.screen.fragment.mypage.paceinfo.PaceSelectItem
import com.applemango.presentation.databinding.ItemPaceCheckboxBinding
import com.applemango.presentation.databinding.ItemPaceRadioBinding

class PaceSimpleSelectListViewHolder(
    val binding: ItemPaceRadioBinding,
    val listener: PaceSelectListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PaceSelectItem) {
        binding.item = item
        binding.root.setOnClickListener {
            listener.itemClick(item)
        }
    }
}

class PaceCheckSelectListViewHolder(
    val binding: ItemPaceCheckboxBinding,
    val listener: PaceSelectListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PaceSelectItem) {
        binding.item = item
        binding.root.setOnClickListener {
            listener.itemClick(item)
        }
    }
}