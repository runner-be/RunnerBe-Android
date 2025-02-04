package com.applemango.presentation.ui.screen.fragment.map.write

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressData(
    val placeName: String,
    val roadAddress: String,
    val detailAddress: String,
    val latitude: String,
    val longitude: String
) : Parcelable {

    fun getFullAddress(): String = "$roadAddress $detailAddress"
}