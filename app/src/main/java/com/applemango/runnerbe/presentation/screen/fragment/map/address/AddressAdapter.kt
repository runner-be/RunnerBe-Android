package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.applemango.runnerbe.databinding.ItemAddressBinding
import javax.inject.Inject

class AddressAdapter @Inject constructor() :
    PagingDataAdapter<AddressResult, AddressViewHolder>(addressDiffUtil) {
    private lateinit var onAddressClickListener: OnAddressClickListener

    companion object {
        private val addressDiffUtil = object : DiffUtil.ItemCallback<AddressResult>() {
            override fun areItemsTheSame(oldItem: AddressResult, newItem: AddressResult): Boolean {
                return oldItem.roadAddress == newItem.roadAddress
            }

            override fun areContentsTheSame(
                oldItem: AddressResult,
                newItem: AddressResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AddressViewHolder(ItemAddressBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onAddressClickListener)
        }
    }

    fun setOnAddressClickListener(listener: OnAddressClickListener) {
        this.onAddressClickListener = listener
    }
}