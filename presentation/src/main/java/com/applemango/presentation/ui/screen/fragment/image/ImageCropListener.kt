package com.applemango.presentation.ui.screen.fragment.image

import android.net.Uri

fun interface ImageCropListener {
    fun onImageCropped(image: Uri)
}