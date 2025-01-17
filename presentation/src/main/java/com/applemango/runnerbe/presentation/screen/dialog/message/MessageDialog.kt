package com.applemango.runnerbe.presentation.screen.dialog.message

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogMessageBinding

class MessageDialog(context : Context) : AlertDialog(context, R.style.confirmDialogStyle) {

    val binding : DialogMessageBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_message,
            null,
            false
        )
    }

    private val data : MutableLiveData<MessageDialogData> = MutableLiveData()

    companion object {
        fun createShow(
            context: Context,
            message: String,
            buttonText : String,
            event : () -> Unit= { }
        ) {
            val dialog = MessageDialog(context)
            with(dialog) {
                data.value = MessageDialogData(
                    message = message,
                    buttonText = buttonText,
                    event = {
                        event()
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