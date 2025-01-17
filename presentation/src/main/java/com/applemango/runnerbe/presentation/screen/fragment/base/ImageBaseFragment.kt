package com.applemango.runnerbe.presentation.screen.fragment.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.applemango.runnerbe.util.simpleDateFormatted
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

open class ImageBaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) : BaseFragment<T>(layoutId) {

    private var isMultipleImage: Boolean = false
    private var mCurrentPhotoPath: String = ""
    var isImage = true

    //앱 설정화면으로 이동한 후, 돌아왔을 때
    private val settingsActionReqLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val permission = if(isImage) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
                else Manifest.permission.READ_EXTERNAL_STORAGE
            } else Manifest.permission.CAMERA

            if (requireActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                if (isImage) openImageGallery()
                else captureCamera()
            }
        }

    val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                if (isImage) openImageGallery()
                else captureCamera()
            } else {
                //앱 설정 화면으로 이동하게 만들자
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                requireActivity().packageName.also { name ->
                    val uri = Uri.fromParts("package", name, null)
                    intent.data = uri
                }
                settingsActionReqLauncher.launch(intent)
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val dataList = ArrayList<Uri>()
                if (it.data!!.clipData == null) {
                    it.data?.data?.let { uri -> dataList.add(uri) }
                } else {
                    val clipData = it.data!!.clipData!!
                    val fileCount = clipData.itemCount
                    for (index in 0 until fileCount) dataList.add(clipData.getItemAt(index).uri)
                }
                resultImageSelect(dataList)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && mCurrentPhotoPath.isNotBlank()) {
                resultCameraCapture(File(mCurrentPhotoPath))
            }
        }

    open fun resultImageSelect(dataList: ArrayList<Uri>) {}

    open fun resultCameraCapture(image: File) {}

    private fun openImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultipleImage)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        imageLauncher.launch(intent)
    }
    private fun captureCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 인텐트를 처리 할 카메라 액티비티가 있는지 확인
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {

            // 촬영한 사진을 저장할 파일 생성
            var photoFile: File? = null
            try {
                //임시로 사용할 파일이므로 경로는 캐시폴더로
                val tempDir: File = requireActivity().cacheDir
                //임시촬영파일 세팅
                val timeStamp: String = Date().simpleDateFormatted("yyyyMMdd")
                val imageFileName = "Capture_" + timeStamp + "_" //ex) Capture_20201206_
                val tempImage = File.createTempFile(
                    imageFileName,  /* 파일이름 */
                    ".jpg",  /* 파일형식 */
                    tempDir /* 경로 */
                )

                // ACTION_VIEW 인텐트를 사용할 경로 (임시파일의 경로)
                mCurrentPhotoPath = tempImage.absolutePath
                photoFile = tempImage
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //파일이 정상적으로 생성되었다면 계속 진행
            if (photoFile != null) {
                //Uri 가져오기
                val photoURI = FileProvider.getUriForFile(
                    requireContext(), requireActivity().packageName.toString() + ".provider",
                    photoFile
                )
                //인텐트에 Uri담기
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                cameraLauncher.launch(takePictureIntent)
            }
        }
    }

}