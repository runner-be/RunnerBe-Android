package com.applemango.runnerbe.presentation.screen.dialog.selectitem

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemSelectBinding

class SelectListItemViewHolder(val binding: ItemSelectBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SelectListData) {
        binding.item = item
    }
}