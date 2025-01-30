package com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo

import com.applemango.runnerbe.presentation.model.type.Pace

data class PaceSelectItem(
    val pace: Pace,
    val paceImageResource: Int,
    val paceName: String,
    val paceDescription : String,
    var isSelected: Boolean
)