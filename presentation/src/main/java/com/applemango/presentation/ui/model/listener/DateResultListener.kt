package com.applemango.presentation.ui.model.listener

import com.applemango.presentation.ui.screen.dialog.dateselect.DateSelectData
import java.util.Date

interface DateResultListener {
    fun getDate(date : Date, displayDate : DateSelectData)
}