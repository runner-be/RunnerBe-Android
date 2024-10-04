package com.applemango.runnerbe.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Pace(val key: String, val time: String): Parcelable {
    ALL("any", "전체 선택"),
    BEGINNER("beginner","700 ~ 900"),
    AVERAGE("average", "600 ~ 700"),
    HIGH("high", "430 ~ 600"),
    MASTER("master", "430 이하");

    companion object {
        fun getPaceByName(name: String?) : Pace? = Pace.values().firstOrNull { it.key == name }
    }
}

