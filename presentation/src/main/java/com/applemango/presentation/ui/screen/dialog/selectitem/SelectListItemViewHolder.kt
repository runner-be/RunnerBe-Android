package com.applemango.presentation.ui.screen.dialog.selectitem

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.databinding.ItemSelectBinding

class SelectListItemViewHolder(val binding: ItemSelectBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SelectListData) {
        binding.item = item
    }
}