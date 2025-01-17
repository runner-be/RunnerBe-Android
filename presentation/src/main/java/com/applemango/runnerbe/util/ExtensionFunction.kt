package com.applemango.runnerbe.util

import android.content.Context
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.applemango.runnerbe.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.imageSrcCompatResource(resourceId: Int) {
    setImageResource(resourceId)
}

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

fun Date.simpleDateFormatted(format: String): String = SimpleDateFormat(format, Locale.KOREA).format(this)

fun File.toUri(context: Context): Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", this)

fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}