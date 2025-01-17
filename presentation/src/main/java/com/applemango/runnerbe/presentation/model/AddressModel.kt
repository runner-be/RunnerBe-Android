package com.applemango.runnerbe.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressModel (
    val placeName: String,
    val roadAddress: String,
    val latitude: String,
    val longitude: String,
    val pageNumber: Int,
): Parcelable