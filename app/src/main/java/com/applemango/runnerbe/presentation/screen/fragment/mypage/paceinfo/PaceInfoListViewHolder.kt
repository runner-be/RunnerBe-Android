package com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applemango.runnerbe.databinding.ItemPaceSelectBinding
import com.applemango.runnerbe.presentation.model.listener.PaceSelectListener

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