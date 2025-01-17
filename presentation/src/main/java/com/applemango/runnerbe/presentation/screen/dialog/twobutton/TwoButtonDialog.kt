package com.applemango.runnerbe.presentation.screen.dialog.twobutton

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogTwoButtonBinding

class TwoButtonDialog(context: Context) : AlertDialog(context, R.style.confirmDialogStyle) {

    val binding: DialogTwoButtonBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_two_button,
            null,
            false
        )
    }

    private val data: MutableLiveData<TwoButtonDialogData> = MutableLiveData()

    companion object {
        fun createShow(
            context: Context,
            title: String,
            firstButtonText: String,
            secondButtonText: String,
            firstEvent: () -> Unit = {},
            secondEvent: () -> Unit = {}
        ) {
            val alertDialog = TwoButtonDialog(context)
            with(alertDialog) {
                data.value = TwoButtonDialogData(
                    title = title,
                    firstButtonText = firstButtonText,
                    firstEvent = {
                        firstEvent()
                        dismiss()
                    },
                    secondButtonText = secondButtonText,
                    secondEvent = {
                        secondEvent()
                        dismiss()
                    }
                )
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.item = data.value
    }
}