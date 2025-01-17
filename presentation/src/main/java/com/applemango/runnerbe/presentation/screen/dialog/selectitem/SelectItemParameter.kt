package com.applemango.runnerbe.presentation.screen.dialog.selectitem

data class SelectItemParameter(
    val itemName: String,
    val event : () -> Unit
)