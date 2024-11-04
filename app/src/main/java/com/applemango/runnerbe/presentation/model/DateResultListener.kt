package com.applemango.runnerbe.presentation.model

import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import java.util.Date

interface DateResultListener {
    fun getDate(date : Date, displayDate : DateSelectData)
}