package com.applemango.presentation.ui.screen.dialog.twobutton

import android.view.View

data class TwoButtonDialogData(
    var secondEvent: View.OnClickListener,
    var firstEvent: View.OnClickListener,
    var title: String,
    var firstButtonText: String,
    var secondButtonText: String
)