package com.applemango.runnerbe.util.glide

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import androidx.core.content.ContextCompat
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.util.dpToPx
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.nio.ByteBuffer
import java.security.MessageDigest

class GranularRoundedAndBorderTransform(
    private val bottomLeft: Float,
    private val bottomRight: Float,
    private val topLeft : Float,
    private val topRight: Float,
    private val strokeWidth: Float,
    private val strokeColorResource: Int
) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val radiusData = ByteBuffer.allocate(16).putFloat(topLeft).putFloat(topRight).putFloat(
            bottomRight
        ).putFloat(bottomLeft).array()
        messageDigest.update(radiusData)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, p2: Int, p3: Int): Bitmap {
        return transformBitmap(TransformationUtils.roundedCorners(pool, toTransform, this.topLeft, this.topRight, this.bottomRight, this.bottomLeft))
    }

    private fun transformBitmap(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height

        val context = RunnerBeApplication.ApplicationContext()
        val tl = topLeft.dpToPx(context)
        val tr = topRight.dpToPx(context)
        val bl = bottomLeft.dpToPx(context)
        val br = bottomRight.dpToPx(context)
        // 새로운 Bitmap 생성
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // 페인트 설정
        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        val path = Path().apply {
            reset()
            moveTo(0f, tl)
            quadTo(0f, 0f, tl, 0f)
            lineTo(width - tr, 0f)
            quadTo(width.toFloat(), 0f, width.toFloat(), tr)
            lineTo(width.toFloat(), height - br)
            quadTo(width.toFloat(), height.toFloat(), width - br, height.toFloat())
            lineTo(bl, height.toFloat())
            quadTo(0f, height.toFloat(), 0f, height - bl)
            close()
        }

        // 그림 그리기
        canvas.drawPath(path, paint)

        // 테두리 그리기
        val borderPaint = Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(RunnerBeApplication.instance.applicationContext, strokeColorResource)
            style = Paint.Style.STROKE
            strokeWidth = this@GranularRoundedAndBorderTransform.strokeWidth // 원하는 테두리 두께 설정
        }
        canvas.drawPath(path, borderPaint)

        return output

    }
}