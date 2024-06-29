package com.applemango.runnerbe.presentation.screen.fragment.mypage

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentMypageBinding
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.screen.fragment.base.ImageBaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.toUri
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MyPageFragment : ImageBaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    View.OnClickListener {

    private val viewModel: MyPageViewModel by viewModels()

    private val mainViewModel : MainViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    lateinit var viewpagerFragmentAdapter: MyPageAdapter

    //이 두개를 사용하는 부분은 추후에 fragment가 아니라 viewModel 및 다른 클래스에서 처리하도록 작성하자
    lateinit var reference: StorageReference
    private val storage: FirebaseStorage = Firebase.storage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myPageViewModel = viewModel
        tabInit()
        observeBind()
        requireActivity()
            .supportFragmentManager
            .setFragmentResultListener("refresh", viewLifecycleOwner) { _, _ -> refresh() }
        binding.settingButton.setOnClickListener(this)
        binding.userProfileEditButton.setOnClickListener(this)
        binding.userImgEdit.setOnClickListener(this)
    }
    override fun onResume() {
        super.onResume()
        if(viewModel.userInfo.value == null) refresh()
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isShowInfoDialog.emit(true)
        }
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.collect {
                when(it) {
                    is MyPageAction.MoveToPaceRegistration -> {
                        navigate(MainFragmentDirections.moveToPaceInfoFragment("myPage"))
                    }
                }
            }
        }
        viewModel.updateUserImageState.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it is UiState.Loading) showLoadingDialog(context)
                else dismissLoadingDialog()
            }
            when (it) {
                is UiState.NetworkError -> {
                    //오프라인 발생 어쩌구 다이얼로그
                }
                is UiState.Failed -> {
                    context?.let { context ->
                        MessageDialog.createShow(
                            context = context,
                            message = it.message,
                            buttonText = resources.getString(R.string.confirm)
                        )
                    }
                }
                is UiState.Success -> {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.complete_change_profile_image),
                        Toast.LENGTH_SHORT
                    ).show()
                    refresh()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moveTab.collect { mainViewModel.setTab(it) }
        }
    }
    private fun refresh() {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        if (userId > -1) { viewModel.getUserData(userId) }
    }

    private fun tabInit() {
        val tabTitles = listOf(
            resources.getString(R.string.written_post),
            resources.getString(R.string.join_running)
        )
        viewpagerFragmentAdapter = MyPageAdapter(this)
        binding.viewPager.adapter = viewpagerFragmentAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun resultCameraCapture(image: File) {
        super.resultCameraCapture(image)
        context?.let {
            uploadImg(image.toUri(it))
        }
    }

    override fun resultImageSelect(dataList: ArrayList<Uri>) {
        super.resultImageSelect(dataList)
        if(dataList.isNotEmpty()) {
            uploadImg(dataList[0])
        }
    }

    private fun uploadImg( uri: Uri) {
        showLoadingDialog(requireContext())
//        firebase storage 에 이미지 업로드하는 method
        var uploadTask: UploadTask? = null // 파일 업로드하는 객체
        val name = RunnerBeApplication.mTokenPreference.getUserId().toString() + "_.png"
        reference = storage.reference.child("item").child(name) // 이미지 파일 경로 지정 (/item/imageFileName)
        uploadTask = uri.let { reference.putFile(it) } // 업로드할 파일과 업로드할 위치 설정
        uploadTask.addOnSuccessListener {
            downloadUri() // 업로드 성공 시 업로드한 파일 Uri 다운받기
            dismissLoadingDialog()
        }.addOnFailureListener {
            it.printStackTrace()
            dismissLoadingDialog()
        }
    }

    private fun downloadUri() {
//        지정한 경로(reference)에 대한 uri 을 다운로드하는 method
        reference.downloadUrl.addOnSuccessListener {
            viewModel.userProfileImageChange(it.toString())
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.settingButton -> {
                navigate(MainFragmentDirections.actionMainFragmentToSettingFragment(viewModel.userInfo.value?.pushOn == "Y"))
            }
            binding.userProfileEditButton -> {
                checkAdditionalUserInfo {
                    viewModel.userInfo.value?.let {
                        navigate(
                            MainFragmentDirections.actionMainFragmentToEditProfileFragment(it)
                        )
                    }
                }
            }
            binding.userImgEdit -> {
                checkAdditionalUserInfo {
                    context?.let {
                        SelectItemDialog.createShow(it, listOf(
                            SelectItemParameter("촬영하기") {
                                isImage = false
                                permReqLauncher.launch(Manifest.permission.CAMERA)
                            },
                            SelectItemParameter("앨범에서 선택하기") {
                                isImage = true
                                permReqLauncher.launch(
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    else Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            },
                            SelectItemParameter("기본 이미지로 변경하기") {
                                viewModel.userProfileImageChange(null)
                            }
                        ))
                    }
                }
            }
        }
    }
}