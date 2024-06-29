package com.applemango.runnerbe.presentation.screen.dialog.pace

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogOneButtonImageTextBinding

class OneButtonImageTextDialog(context: Context) : Dialog(context, R.style.confirmDialogStyle) {

    private val binding: DialogOneButtonImageTextBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_one_button_image_text,
            null,
            false
        )
    }

    private var data : OneButtonImageTextData? = null
    private var listener : OneButtonClickListener? = null
    companion object {

        fun createShow(
            context: Context,
            selectedData: OneButtonImageTextData? = null,
            resultListener: OneButtonClickListener
        ) {
            val dialog = OneButtonImageTextDialog(context)
            with(dialog) {
                this.data = selectedData
                this.listener = resultListener
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.data = data
        setContentView(binding.root)
        dialogResize(context, 0.8f)
        setCancelable(false)
        binding.confirmButton.setOnClickListener {
            listener?.onClicked()
            dismiss()
        }
    }

    private fun dialogResize(context: Context, width: Float){
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = binding.root.layoutParams
        val x= if (Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            (size.x * width).toInt()
        }else{
            val rect = windowManager.currentWindowMetrics.bounds
            (rect.width() * width).toInt()
        }
        layoutParams.width = x
        binding.root.layoutParams = layoutParams
    }
}