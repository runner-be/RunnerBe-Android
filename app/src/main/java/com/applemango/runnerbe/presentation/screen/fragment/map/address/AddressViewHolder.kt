package com.applemango.runnerbe.presentation.screen.fragment.map.address

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemAddressBinding

class AddressViewHolder (
    private val binding: ItemAddressBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AddressResult, onAddressClickListener: OnAddressClickListener) {
        binding.item = item
        binding.llAddress.setOnClickListener {
            onAddressClickListener.onAddressClicked(item)
        }
    }
}