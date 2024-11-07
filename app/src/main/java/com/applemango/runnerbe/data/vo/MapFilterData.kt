package com.applemango.runnerbe.data.vo

import android.os.Parcelable
import com.applemango.runnerbe.domain.entity.Pace
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapFilterData(
    val paceTags: List<Pace>,
    val genderTag: String,
    val afterPartyTag: String,
    val jobTag: String,
    val minAge: Int,
    val maxAge: Int
) : Parcelable {
    companion object {
        fun isFilterApplied(data: MapFilterData): Boolean =
            data != MapFilterData(
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
