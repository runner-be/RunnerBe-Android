package com.applemango.runnerbe.presentation.screen.dialog.message

import android.view.View

data class YesNoButtonDialogData(
    var positiveEvent: View.OnClickListener,
    var negativeEvent: View.OnClickListener,
    var message: String,
    var positiveButtonText: String,
    var negativeButtonText: String
)
