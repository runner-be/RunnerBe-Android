package com.applemango.runnerbe.presentation.screen.fragment.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageManager {
    companion object {
        private val PROVIDER = "com.applemango.runnerbe.provider"

        fun parseBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
            return try {
                val file = File(context.cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.flush()
                outputStream.close()

                FileProvider.getUriForFile(
                    context,
                    PROVIDER,
                    file
                )
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}