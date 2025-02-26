package com.applemango.presentation.ui.screen.fragment.mypage.paceinfo

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applemango.presentation.ui.model.listener.PaceSelectListener
import com.applemango.presentation.databinding.ItemPaceSelectBinding

class PaceInfoListViewHolder(
    val binding: ItemPaceSelectBinding,
    val listener : PaceSelectListener
): ViewHolder(binding.root) {

    fun bind(item: PaceSelectItem) {
        binding.data = item
        binding.root.setOnClickListener {
            listener.itemClick(item)
        }
    }
}