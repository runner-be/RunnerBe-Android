package com.applemango.presentation.ui.screen.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.applemango.presentation.R.*

class LoadingDialog(context: Context) : Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layout.dialog_loading)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.2f)
    }

    override fun show() {
        if(!this.isShowing) super.show()
    }
}