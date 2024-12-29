package com.applemango.runnerbe.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileUrlList(
    @Json(name = "userId") val userId: Int,
    @Json(name = "profileImageUrl") val profileImageUrl: String?
): Parcelable