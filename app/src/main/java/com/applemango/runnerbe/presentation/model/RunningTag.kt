package com.applemango.runnerbe.presentation.model

import com.applemango.runnerbe.R

enum class RunningTag(val tag: String) {
    All("W"),
    After("A"),
    Before("B"),
    Holiday("H");

    companion object {
        fun getByTag(tag: String) : RunningTag? = values().find { it.tag == tag }
    }

    fun getTagNameResource() : Int {
        return when(tag) {
            All.tag -> R.string.all_work
            After.tag -> R.string.after_work
            Holiday.tag -> R.string.holiday
            else -> R.string.before_work
        }
    }
}