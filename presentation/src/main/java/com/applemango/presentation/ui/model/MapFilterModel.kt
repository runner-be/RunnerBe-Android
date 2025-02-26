package com.applemango.presentation.ui.model

import android.os.Parcelable
import com.applemango.presentation.ui.model.type.Pace
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapFilterModel(
    val paceTags: List<Pace>,
    val genderTag: String,
    val afterPartyTag: String,
    val jobTag: String,
    val minAge: Int,
    val maxAge: Int
) : Parcelable {
    companion object {
        fun isFilterApplied(data: MapFilterModel): Boolean =
            data != MapFilterModel(
                listOf(
                    Pace.ALL,
                    Pace.BEGINNER,
                    Pace.AVERAGE,
                    Pace.HIGH,
                    Pace.MASTER
                ), "A", "A", "N", 0, 100
            )
    }
}
