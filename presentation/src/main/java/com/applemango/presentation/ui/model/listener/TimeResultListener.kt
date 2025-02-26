package com.applemango.presentation.ui.model.listener

import com.applemango.presentation.ui.screen.dialog.timeselect.TimeSelectData

fun interface TimeResultListener {
    fun getDate(displayTime : TimeSelectData)
}