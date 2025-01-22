package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectData

fun interface TimeResultListener {
    fun getDate(displayTime : TimeSelectData)
}