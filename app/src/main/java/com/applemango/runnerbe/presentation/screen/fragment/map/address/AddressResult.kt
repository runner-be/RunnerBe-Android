package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressResult (
    val placeName: String,
    val roadAddress: String,
    val x: String,
    val y: String,
    val pageNumber: Int,
): Parcelable