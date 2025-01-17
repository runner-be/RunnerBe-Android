package com.applemango.runnerbe.presentation.screen.fragment.image

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ActivityImageCropBinding
import com.applemango.runnerbe.presentation.model.type.CropRectRatio
import com.applemango.runnerbe.presentation.screen.activity.BaseActivity
import com.applemango.runnerbe.presentation.screen.dialog.message.YesNoButtonDialog
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ImageCropActivity : BaseActivity<ActivityImageCropBinding>(R.layout.activity_image_crop) {
    private val viewModel: ImageCropViewModel by viewModels()

    private var _compositeDisposable: CompositeDisposable? = null
    val compositeDisposable: CompositeDisposable get() = _compositeDisposable ?: throw IllegalStateException("CompositeDisposable is NULL")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            ratio = CropRectRatio.RATIO_1_1
        }
        getNavArgs(intent)
        _compositeDisposable = CompositeDisposable()
        initListeners()
    }

    override fun onDestroy() {
        _compositeDisposable = null
        super.onDestroy()
    }

    private fun getNavArgs(intent: Intent) {
        intent.getStringExtra("IMAGE_URI")?.let {
            binding.customImageCropView.loadInitialImageByUri(it)
        }
    }

    private fun askAndDismissDialog() {
        YesNoButtonDialog.createShow(
            this,
            getString(R.string.dialog_image_crop_exit),
            getString(R.string.yes),
            getString(R.string.no),
            positiveEvent = fun() {
                finish()
            },
            negativeEvent = fun() {

            }
        )
    }

    private fun askAndSaveDialog() {
        YesNoButtonDialog.createShow(
            this,
            getString(R.string.dialog_image_crop_save),
            getString(R.string.yes),
            getString(R.string.no),
            positiveEvent = fun() {
                val croppedBitmap = binding.customImageCropView.getCroppedBitmap()
                if (croppedBitmap != null) {
                    val croppedImageUri = ImageManager.parseBitmapToUri(this, croppedBitmap)
                    if (croppedImageUri != null) {
                        val resultIntent = Intent().apply {
                            putExtra("CROPPED_IMAGE_URI", croppedImageUri.toString())
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
            },
            negativeEvent = fun() {

            }
        )
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.tvCancel.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    askAndDismissDialog()
                },
            binding.tvComplete.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    askAndSaveDialog()
                },
            binding.tvRatio11.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    binding.customImageCropView.updateCurrentRatio(CropRectRatio.RATIO_1_1)
                    binding.ratio = CropRectRatio.RATIO_1_1
                },
            binding.tvRatio43.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    binding.customImageCropView.updateCurrentRatio(CropRectRatio.RATIO_4_3)
                    binding.ratio = CropRectRatio.RATIO_4_3
                },
            binding.tvRatioFill.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    binding.customImageCropView.updateCurrentRatio(CropRectRatio.RATIO_FILL)
                    binding.ratio = CropRectRatio.RATIO_FILL
                },
        )
    }
}