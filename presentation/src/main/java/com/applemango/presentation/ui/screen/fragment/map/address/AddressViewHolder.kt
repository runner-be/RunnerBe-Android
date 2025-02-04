package com.applemango.presentation.ui.screen.fragment.map.address

import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.AddressModel
import com.applemango.presentation.databinding.ItemAddressBinding

class AddressViewHolder (
    private val binding: ItemAddressBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AddressModel, onAddressClickListener: OnAddressClickListener) {
        binding.item = item
        binding.llAddress.setOnClickListener {
            onAddressClickListener.onAddressClicked(item)
        }
    }
}