package com.applemango.runnerbe.presentation.screen.fragment.map.write

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressData(
    val placeName: String,
    val roadAddress: String,
    val detailAddress: String,
    val x: String,
    val y: String
) : Parcelable