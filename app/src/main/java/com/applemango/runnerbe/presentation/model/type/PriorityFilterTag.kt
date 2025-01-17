package com.applemango.runnerbe.presentation.model.type

import com.applemango.runnerbe.R

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