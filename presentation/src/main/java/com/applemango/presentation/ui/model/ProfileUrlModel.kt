package com.applemango.presentation.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileUrlModel(
    val userId: Int,
    val profileImageUrl: String?
): Parcelable