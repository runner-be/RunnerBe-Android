package com.applemango.runnerbe.presentation.model.type

import com.applemango.runnerbe.R

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