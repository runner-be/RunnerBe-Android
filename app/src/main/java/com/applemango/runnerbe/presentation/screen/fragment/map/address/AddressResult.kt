package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressResult (
    val placeName: String,
    val roadAddress: String,
    val latitude: String,
    val longitude: String,
    val pageNumber: Int,
): Parcelable