package com.applemango.runnerbe.presentation.screen.fragment.image

import android.net.Uri

fun interface ImageCropListener {
    fun onImageCropped(image: Uri)
}