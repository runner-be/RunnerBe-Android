package com.applemango.presentation.util.manager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.applemango.presentation.ui.screen.dialog.camera.CameraDialog
import com.applemango.presentation.ui.screen.dialog.camera.CameraOptionSelectListener
import com.applemango.presentation.ui.screen.fragment.image.ImageCropActivity
import com.applemango.presentation.ui.screen.fragment.image.ImageCropListener
import com.applemango.presentation.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class SinglePhotoManager @Inject constructor(
    fragment: Fragment,
    private val imageCropListener: ImageCropListener
) {
    private val context: Context = fragment.requireContext()

    private var selectedPhoto: Uri = Uri.EMPTY
        set(value) {
            if (value == field) return
//            viewModel.updateLogImage(value)
            field = value
        }

    private val cameraLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                processSelectedPhoto(result)
            }
        }

    private val cropImageLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val croppedImageUriString = data.getStringExtra("CROPPED_IMAGE_URI")
                    val croppedImageUri: Uri? = croppedImageUriString?.let { Uri.parse(it) }
                    if (croppedImageUri != null) {
                        selectedPhoto = croppedImageUri
                        imageCropListener.onImageCropped(croppedImageUri)
                    }
                }
            }
        }

    fun showCameraDialog() {
        CameraDialog.createAndShow(
            context,
            object : CameraOptionSelectListener {
                override fun onCaptureButtonClicked() {
                    createCaptureIntent()
                }

                override fun onAlbumButtonClicked() {
                    createAlbumIntent()
                }
            }
        )
    }

    private fun createCaptureIntent() {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            PermissionManager.requestPermission(
                Manifest.permission.CAMERA,
                grantCallback = {
                    getCameraIntent()
                },
                deniedCallback = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.permission_denied_camera),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        } else {
            getCameraIntent()
        }
    }

    private fun getCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .also { captureIntent ->
                captureIntent.resolveActivity(context.packageManager)?.let {
                    val photoFile: File? = try {
                        createTempImageFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        null
                    }

                    val photoUri = photoFile?.let {
                        try {
                            FileProvider.getUriForFile(
                                context,
                                "com.applemango.runnerbe.provider",
                                it
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            null
                        }
                    }

                    if (photoUri != null) {
                        selectedPhoto = photoUri
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        cameraLauncher.launch(captureIntent)
                    }
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createTempImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir((Environment.DIRECTORY_PICTURES))
        return File.createTempFile(
            "JPED_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun createAlbumIntent() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(Intent.ACTION_GET_CONTENT)
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        }.apply {
            type = INTENT_TYPE_IMAGE
        }
        cameraLauncher.launch(intent)
    }

    private fun processSelectedPhoto(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            if (selectedPhoto != Uri.EMPTY) {
                imageCropListener.onImageCropped(selectedPhoto)
//                startCropActivity(selectedPhoto)
            } else {
                // 앨범에서 선택한 경우
                result.data?.let { photo ->
                    val uri = photo.data
                    if (uri != null) {
                        selectedPhoto = uri
                        imageCropListener.onImageCropped(selectedPhoto)
//                        startCropActivity(selectedPhoto)
                    }
                }
            }
        }
    }

    private fun startCropActivity(imageUri: Uri) {
        val cropIntent = Intent(context, ImageCropActivity::class.java).apply {
            putExtra("IMAGE_URI", imageUri.toString())
        }
        cropImageLauncher.launch(cropIntent)
    }

    companion object {
        private const val INTENT_TYPE_IMAGE = "image/*"
    }
}