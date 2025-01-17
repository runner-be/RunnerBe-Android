package com.applemango.runnerbe.presentation.screen.fragment.map.address

import com.applemango.runnerbe.presentation.model.AddressModel

fun interface OnAddressClickListener {
    fun onAddressClicked(address: AddressModel)
}