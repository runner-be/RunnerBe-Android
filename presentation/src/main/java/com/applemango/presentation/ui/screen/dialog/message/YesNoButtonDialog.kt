package com.applemango.presentation.ui.screen.dialog.message

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogYesNoBinding

class YesNoButtonDialog (
    context: Context
) : AlertDialog(context, R.style.confirmDialogStyle) {

    val binding : DialogYesNoBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_yes_no,
            null,
            false
        )
    }

    private val data : MutableLiveData<YesNoButtonDialogData> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.item = data.value
    }

    companion object {
        fun createShow(
            context: Context,
            message: String,
            positiveButtonText: String,
            negativeButtonText: String,
            positiveEvent : () -> Unit= { },
            negativeEvent : () -> Unit= { }
        ) {
            val dialog = YesNoButtonDialog(context)
            with(dialog) {
                data.value = YesNoButtonDialogData(
                    message = message,
                    positiveEvent = {
                        positiveEvent()
                        dismiss()
                    },
                    negativeEvent = {
                        negativeEvent()
                        dismiss()
                    },
                    positiveButtonText = positiveButtonText,
                    negativeButtonText = negativeButtonText
                )
                show()
            }
        }
    }
}