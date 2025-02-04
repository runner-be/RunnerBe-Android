package com.applemango.presentation.ui.screen.dialog.selectitem

data class SelectItemParameter(
    val itemName: String,
    val event : () -> Unit
)