package com.applemango.runnerbe.presentation.screen.fragment.map.write

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressData(
    val placeName: String,
    val roadAddress: String,
    val detailAddress: String,
    val longitude: String,
    val latitude: String
) : Parcelable {

    fun getFullAddress(): String = "$roadAddress $detailAddress"
}