package com.applemango.presentation.ui.screen.fragment.map.address

import com.applemango.presentation.ui.model.AddressModel

fun interface OnAddressClickListener {
    fun onAddressClicked(address: AddressModel)
}