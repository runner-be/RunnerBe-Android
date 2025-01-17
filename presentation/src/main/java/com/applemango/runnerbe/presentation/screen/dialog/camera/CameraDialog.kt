package com.applemango.runnerbe.presentation.screen.dialog.camera

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogCameraBinding

class CameraDialog(
    context: Context,
    cameraOptionSelectListener: CameraOptionSelectListener
) : AlertDialog(context, R.style.confirmDialogStyle), View.OnClickListener {
    private var cameraListener: CameraOptionSelectListener

    private val binding: DialogCameraBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_camera,
            null,
            false
        )
    }

    init {
        this.cameraListener = cameraOptionSelectListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            btnCapture.setOnClickListener(this@CameraDialog)
            btnAlbum.setOnClickListener(this@CameraDialog)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCapture -> {
                cameraListener.onCaptureButtonClicked()
                dismiss()
            }

            binding.btnAlbum -> {
                cameraListener.onAlbumButtonClicked()
                dismiss()
            }
        }
    }

    companion object {
        fun createAndShow(
            context: Context,
            cameraOptionSelectListener: CameraOptionSelectListener
        ) {
            val dialog = CameraDialog(context, cameraOptionSelectListener)
            dialog.show()
        }
    }
}