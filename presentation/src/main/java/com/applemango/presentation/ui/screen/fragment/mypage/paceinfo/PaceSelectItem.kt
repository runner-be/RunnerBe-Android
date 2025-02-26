package com.applemango.presentation.ui.screen.fragment.mypage.paceinfo

import com.applemango.presentation.ui.model.type.Pace

data class PaceSelectItem(
    val pace: Pace,
    val paceImageResource: Int,
    val paceName: String,
    val paceDescription : String,
    var isSelected: Boolean
)