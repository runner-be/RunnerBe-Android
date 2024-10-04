package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemAddressBinding

class AddressViewHolder (
    private val binding: ItemAddressBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AddressResult, onAddressClickListener: OnAddressClickListener) {
        Log.e("Address search", " : $item")
        with(binding) {
            tvAddressMain.text = item.placeName
            tvAddressSub.text = item.roadAddress
            llAddress.setOnClickListener {
                onAddressClickListener.onAddressClicked(item)
            }
        }
    }
}