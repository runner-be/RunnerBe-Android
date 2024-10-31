package com.applemango.runnerbe.presentation.screen.fragment.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.applemango.runnerbe.R
import com.applemango.runnerbe.util.LogUtil
import com.applemango.runnerbe.util.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.lang.IllegalStateException

data class LastTouch(
    val x: Float,
    val y: Float
)

/**
 * invalidate() -> 해당 뷰를 다시 그리도록 요청, 다음 UI 갱신 주기에서 다시 그려짐
 */
class ImageCropView(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs) {
    private var bitmap: Bitmap? = null
    private var cropRect: RectF = RectF(100f, 100f, 400f, 400f)
    private var lastTouchXY: LastTouch = LastTouch(0f, 0f)
    private val borderSize: Float = 50f
    private val paint = Paint().apply {
        color = context.getColor(R.color.white)
        style = Paint.Style.STROKE
        strokeWidth = 1.dpToPx(context).toFloat()
    }
    private val imageView: ImageView
    private val minWidth: Float = 100.dpToPx(context).toFloat()
    private val minHeight: Float = 100.dpToPx(context).toFloat()

    private var currentRatio: CropRectRatio = CropRectRatio.RATIO_1_1

    init {
        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        addView(imageView)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val totalWidth = cropRect.right - cropRect.left
        val totalHeight = cropRect.bottom - cropRect.top

        // 양쪽 10% 제외하고 그리기
        val horizontalCutoff = totalWidth * 0.1f
        val verticalCutoff = totalHeight * 0.1f

        // 일반 선 그리기
        paint.strokeWidth = 1.dpToPx(context).toFloat()

        // 윗쪽 선 양 끝 10% 제외하고 그리기
        canvas.drawLine(
            cropRect.left + horizontalCutoff,
            cropRect.top,
            cropRect.right - horizontalCutoff,
            cropRect.top,
            paint
        )

        // 아래쪽 선 양 끝 10% 제외하고 그리기
        canvas.drawLine(
            cropRect.left + horizontalCutoff,
            cropRect.bottom,
            cropRect.right - horizontalCutoff,
            cropRect.bottom,
            paint
        )

        // 왼쪽 선 양 끝 10% 제외하고 그리기
        canvas.drawLine(
            cropRect.left,
            cropRect.top + verticalCutoff,
            cropRect.left,
            cropRect.bottom - verticalCutoff,
            paint
        )

        // 오른쪽 선 양 끝 10% 제외하고 그리기
        canvas.drawLine(
            cropRect.right,
            cropRect.top + verticalCutoff,
            cropRect.right,
            cropRect.bottom - verticalCutoff,
            paint
        )

        // 모서리 강조
        paint.strokeWidth = 3.dpToPx(context).toFloat()

        // 좌측 상단 5%
        canvas.drawLine(
            cropRect.left,
            cropRect.top,
            cropRect.left + horizontalCutoff,
            cropRect.top,
            paint
        )

        // 우측 상단 5%
        canvas.drawLine(
            cropRect.right - horizontalCutoff,
            cropRect.top,
            cropRect.right,
            cropRect.top,
            paint
        )

        // 좌측 하단 5%
        canvas.drawLine(
            cropRect.left,
            cropRect.bottom,
            cropRect.left + horizontalCutoff,
            cropRect.bottom,
            paint
        )

        // 우측 하단 5%
        canvas.drawLine(
            cropRect.right - horizontalCutoff,
            cropRect.bottom,
            cropRect.right,
            cropRect.bottom,
            paint
        )

        // 좌측 상단 5%
        canvas.drawLine(
            cropRect.left,
            cropRect.top,
            cropRect.left,
            cropRect.top + verticalCutoff,
            paint
        )

        // 좌측 하단 5%
        canvas.drawLine(
            cropRect.left,
            cropRect.bottom - verticalCutoff,
            cropRect.left,
            cropRect.bottom,
            paint
        )

        // 우측 상단 5%
        canvas.drawLine(
            cropRect.right,
            cropRect.top,
            cropRect.right,
            cropRect.top + verticalCutoff,
            paint
        )

        // 우측 하단 5%
        canvas.drawLine(
            cropRect.right,
            cropRect.bottom - verticalCutoff,
            cropRect.right,
            cropRect.bottom,
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchXY = LastTouch(touchX, touchY)
                if (cropRect.contains(touchX, touchY)) {
                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val offsetX = touchX - lastTouchXY.x
                val offsetY = touchY - lastTouchXY.y

                if (lastTouchXY.x in cropRect.left .. cropRect.left + borderSize) {
                    resizeByDirection(ResizeDirection.LEFT, offsetX, touchX, touchY)
                    return true
                } else if (lastTouchXY.x in cropRect.right - borderSize..cropRect.right) {
                    resizeByDirection(ResizeDirection.RIGHT, offsetX, touchX, touchY)
                    return true
                } else if (lastTouchXY.y in cropRect.top..cropRect.top + borderSize) {
                    resizeByDirection(ResizeDirection.TOP, offsetY, touchX, touchY)
                    return true
                } else if (lastTouchXY.y in cropRect.bottom - borderSize..cropRect.bottom) {
                    resizeByDirection(ResizeDirection.BOTTOM, offsetY, touchX, touchY)
                    return true
                }

                if (cropRect.contains(lastTouchXY.x, lastTouchXY.y)) {
                    val newLeft = cropRect.left + offsetX
                    val newRight = cropRect.right + offsetX
                    val newTop = cropRect.top + offsetY
                    val newBottom = cropRect.bottom + offsetY

                    val adjustedLeft = newLeft.coerceAtLeast(0f)
                    val adjustedTop = newTop.coerceAtLeast(0f)

                    if (newRight <= imageView.width && newBottom <= imageView.height) {
                        cropRect.offsetTo(adjustedLeft, adjustedTop)
                        invalidate()
                        lastTouchXY = LastTouch(touchX, touchY)
                    }
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun resizeByDirection(
        resizeDirection: ResizeDirection,
        changeValue: Float,
        touchX: Float? = null,
        touchY: Float? = null
    ) {
        val aspectRatio = when (currentRatio) {
            CropRectRatio.RATIO_1_1 -> 1f / 1f
            CropRectRatio.RATIO_4_3 -> 4f / 3f
            CropRectRatio.RATIO_FILL -> 0f
        }

        when (resizeDirection) {
            ResizeDirection.LEFT -> {
                val newLeft = cropRect.left + changeValue
                if (aspectRatio == 0f) {
                    if ((cropRect.right - newLeft) >= minWidth) {
                        cropRect.left = newLeft.coerceAtLeast(0f)
                    }
                } else {
                    val newWidth = cropRect.right - newLeft
                    val newHeight = newWidth / aspectRatio
                    if (newWidth >= minWidth && newHeight >= minHeight && newWidth <= imageView.width && newHeight <= imageView.height) {
                        cropRect.left = newLeft.coerceAtLeast(0f)
                        cropRect.top = (cropRect.bottom - newHeight).coerceAtLeast(0f)
                    }
                }
            }

            ResizeDirection.RIGHT -> {
                val newRight = cropRect.right + changeValue
                if (aspectRatio == 0f) {
                    if ((newRight - cropRect.left) >= minWidth) {
                        cropRect.right = newRight.coerceAtMost(imageView.width.toFloat())
                    }
                } else {
                    val newWidth = newRight - cropRect.left
                    val newHeight = newWidth / aspectRatio
                    if (newWidth >= minWidth && newHeight >= minHeight && newWidth <= imageView.width && newHeight <= imageView.height) {
                        cropRect.right = newRight.coerceAtMost(imageView.width.toFloat())
                        cropRect.top = (cropRect.bottom - newHeight).coerceAtLeast(0f)
                    }
                }
            }

            ResizeDirection.TOP -> {
                val newTop = cropRect.top + changeValue
                if (aspectRatio == 0f) {
                    if ((cropRect.bottom - newTop) >= minHeight) {
                        cropRect.top = newTop.coerceAtLeast(0f)
                    }
                } else {
                    val newHeight = cropRect.bottom - newTop
                    val newWidth = newHeight * aspectRatio
                    if (newHeight >= minHeight && newWidth >= minWidth && newWidth <= imageView.width && newHeight <= imageView.height) {
                        cropRect.top = newTop.coerceAtLeast(0f)
                        cropRect.right = (cropRect.left + newWidth).coerceAtMost(imageView.width.toFloat())
                    }
                }
            }

            ResizeDirection.BOTTOM -> {
                val newBottom = cropRect.bottom + changeValue
                if (aspectRatio == 0f) {
                    if ((newBottom - cropRect.top) >= minHeight) {
                        cropRect.bottom = newBottom.coerceAtMost(imageView.height.toFloat())
                    }
                } else {
                    val newHeight = newBottom - cropRect.top
                    val newWidth = newHeight * aspectRatio
                    if (newHeight >= minHeight && newWidth >= minWidth && newWidth <= imageView.width && newHeight <= imageView.height) {
                        cropRect.bottom = newBottom.coerceAtMost(imageView.height.toFloat())
                        cropRect.right = (cropRect.left + newWidth).coerceAtMost(imageView.width.toFloat())
                    }
                }
            }
        }

        cropRect.left = cropRect.left.coerceAtLeast(0f)
        cropRect.top = cropRect.top.coerceAtLeast(0f)
        cropRect.right = cropRect.right.coerceAtMost(imageView.width.toFloat())
        cropRect.bottom = cropRect.bottom.coerceAtMost(imageView.height.toFloat())

        invalidate()

        if (touchX != null && touchY != null) {
            lastTouchXY = LastTouch(touchX, touchY)
        }
    }

    fun loadInitialImageByUri(uri: String) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object: CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    imageView.setImageBitmap(bitmap)
                    imageView.invalidate()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    fun loadImageBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        imageView.setImageBitmap(bitmap)
        imageView.invalidate()
    }

    fun getCroppedBitmap(): Bitmap? {
        return try {
            val currentBitmap = checkNotNull(this.bitmap)
            val imageViewWidth = imageView.width
            val imageViewHeight = imageView.height

            val left = (cropRect.left * currentBitmap.width / imageViewWidth).toInt()
            val right = (cropRect.right * currentBitmap.width / imageViewWidth).toInt()
            val top = (cropRect.top * currentBitmap.height / imageViewHeight).toInt()
            val bottom = (cropRect.bottom * currentBitmap.height / imageViewHeight).toInt()

            Bitmap.createBitmap(currentBitmap, left, top, right - left, bottom - top)
        } catch (e: IllegalStateException) {
            LogUtil.errorLog("Caused by: ${e::class.java} | 이미지 크롭 실패 | ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun updateCurrentRatio(ratio: CropRectRatio) {
        this.currentRatio = ratio
        val imageViewWidth = imageView.width.toFloat()
        val imageViewHeight = imageView.height.toFloat()

        var left = 0f
        var right = 0f
        var top = 0f
        var bottom = 0f

        when (ratio) {
            CropRectRatio.RATIO_1_1 -> {
                val size = imageViewWidth.coerceAtMost(imageViewHeight) * 0.5f
                val centerX = imageViewWidth / 2f
                val centerY = imageViewHeight / 2f

                left = centerX - size / 2
                right = centerX + size / 2
                top = centerY - size / 2
                bottom = centerY + size / 2
            }

            CropRectRatio.RATIO_4_3 -> {
                val width = imageViewWidth * 0.6f
                val height = width * 3f / 4f
                val centerX = imageViewWidth / 2f
                val centerY = imageViewHeight / 2f

                left = centerX - width / 2
                right = centerX + width / 2
                top = centerY - height / 2
                bottom = centerY + height / 2
            }

            CropRectRatio.RATIO_FILL -> {
                // 이미지뷰를 꽉 채우도록 설정
                left = 0f
                right = imageViewWidth
                top = 0f
                bottom = imageViewHeight
            }
        }

        cropRect.left = left.coerceAtLeast(0f)
        cropRect.right = right.coerceAtMost(imageViewWidth)
        cropRect.top = top.coerceAtLeast(0f)
        cropRect.bottom = bottom.coerceAtMost(imageViewHeight)
        invalidate()
    }
}