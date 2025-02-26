package com.applemango.presentation.ui.model.type

import com.applemango.presentation.R

enum class PriorityFilterTag(val tag: String) {
    BY_DISTANCE("D"),
    NEWEST("R");


    companion object {
        fun getByTag(tag: String) : RunningTag? = RunningTag.values().find { it.tag == tag }
    }

    fun getTagNameResource() : Int {
        return when(tag) {
            BY_DISTANCE.tag -> R.string.by_distance
            else -> R.string.newest
        }
    }

}