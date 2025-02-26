package com.applemango.presentation.ui.model.type

import com.applemango.presentation.R

enum class MainBottomTab {
    MAP,
    BOOK_MARK,
    MESSAGE,
    MY;

    val iconResourceId: Int
        get() {
            return when(this) {
                MAP -> R.drawable.ic_select_home
                BOOK_MARK -> R.drawable.ic_select_book_mark
                MESSAGE -> R.drawable.ic_select_message
                MY -> R.drawable.ic_select_my
            }
        }
}