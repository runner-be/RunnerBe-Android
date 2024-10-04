package com.applemango.runnerbe.data.vo

import android.os.Parcelable
import com.applemango.runnerbe.domain.entity.Pace
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapFilterData(
    val paceTags: List<Pace>,
    val genderTag: String,
    val jobTag: String,
    val minAge: Int,
    val maxAge: Int
) : Parcelable {
    val isFilterApply =
        !(paceTags != listOf(Pace.ALL) && genderTag == "A" && jobTag == "N" && minAge == 0 && maxAge == 100)
}
