package com.applemango.runnerbe.presentation.screen.fragment.mypage

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.FragmentMypageBinding
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.screen.fragment.base.ImageBaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningPostAdapter
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.LogUtil
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.RightSpaceItemDecoration
import com.applemango.runnerbe.util.toUri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment : ImageBaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    View.OnClickListener {

    private val viewModel: MyPageViewModel by activityViewModels()

    private val mainViewModel: MainViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    @Inject
    internal lateinit var joinedRunningPostAdapter: JoinedRunningPostAdapter

    private var _weeklyCalendarPagerAdapter: WeeklyCalendarPagerAdapter? = null
    private val weeklyCalendarPagerAdapter get() = _weeklyCalendarPagerAdapter!!

    //이 두개를 사용하는 부분은 추후에 fragment가 아니라 viewModel 및 다른 클래스에서 처리하도록 작성하자
    lateinit var reference: StorageReference
    private val storage: FirebaseStorage = Firebase.storage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myPageViewModel = viewModel
        observeBind()
        requireActivity()
            .supportFragmentManager
            .setFragmentResultListener("refresh", viewLifecycleOwner) { _, _ -> refresh() }
        initParticipatedRunningAdapter()
        setupJoinedRunningPosts()
        setupThisWeekRunningLogs()
        initYearMonthSpinner()
        setupWeeklyViewPagerPosition()
        binding.constJoinedRunningPost.setOnClickListener(this)
        binding.settingButton.setOnClickListener(this)
        binding.userProfileEditButton.setOnClickListener(this)
        binding.userImgEdit.setOnClickListener(this)
        binding.ivCalendar.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.userInfo.value == null) refresh()
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isShowInfoDialog.emit(true)
        }
    }

    override fun onDestroyView() {
        _weeklyCalendarPagerAdapter = null
        super.onDestroyView()
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.collect {
                when (it) {
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

                else -> {
                    Log.e("MainViewModel", "bookmarkStatusChange - when - else")
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moveTab.collect { mainViewModel.setTab(it) }
        }
    }

    private fun refresh() {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        if (userId > -1) {
            viewModel.getUserData(userId)
        }
    }

    override fun resultCameraCapture(image: File) {
        super.resultCameraCapture(image)
        context?.let {
            uploadImg(image.toUri(it))
        }
    }

    override fun resultImageSelect(dataList: ArrayList<Uri>) {
        super.resultImageSelect(dataList)
        if (dataList.isNotEmpty()) {
            uploadImg(dataList[0])
        }
    }

    private fun uploadImg(uri: Uri) {
        showLoadingDialog(requireContext())
//        firebase storage 에 이미지 업로드하는 method
        var uploadTask: UploadTask? = null // 파일 업로드하는 객체
        val name = RunnerBeApplication.mTokenPreference.getUserId().toString() + "_.png"
        reference =
            storage.reference.child("item").child(name) // 이미지 파일 경로 지정 (/item/imageFileName)
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

    private fun initYearMonthSpinner() {
        with(binding.tvWeek) {
            val today = LocalDate.now()
            text = getString(R.string.calendar_selected_year_month, today.year, today.monthValue)
        }
    }

    private fun setupWeeklyViewPagerPosition() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeeklyViewPagerPosition.collectLatest { position ->
                    LogUtil.errorLog("CurrentViewPager position: $position")
                    position?.let {
                        binding.vpWeeklyCalendar.setCurrentItem(position, false)
                    }
                }
            }
        }
    }

    private fun initWeeklyViewPagerAdapter() {
        binding.vpWeeklyCalendar.apply {
            _weeklyCalendarPagerAdapter = WeeklyCalendarPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
            )
            adapter = weeklyCalendarPagerAdapter
        }
        if (viewModel.currentWeeklyViewPagerPosition.value == null) {
            viewModel.updateWeeklyViewPagerPosition(2)
        }
        initDotsIndicator()
    }

    private fun setupThisWeekRunningLogs() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.thisWeekRunningLogFlow.collectLatest { result ->
                    initWeeklyViewPagerAdapter()
                    result.totalCount?.let {
                        binding.tvStampWeekly.text = getString(
                            R.string.calendar_monthly_statistic,
                            it.groupRunningCount, it.personalRunningCount
                        )
                    }
                }
            }
        }
    }

    private fun setupJoinedRunningPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.joinPosts.collectLatest {
                    joinedRunningPostAdapter.submitList(it)
                }
            }
        }
    }

    private fun initParticipatedRunningAdapter() {
        with(binding.rcvJoinedRunningPost) {
            adapter = joinedRunningPostAdapter
            joinedRunningPostAdapter.setPostClickListener(object : JoinedRunningClickListener {
                override fun logWriteClick(post: Posting) {

                }

                override fun attendanceSeeClick(post: Posting) {

                }

                override fun attendanceManageClick(post: Posting) {

                }

                override fun bookMarkClick(post: Posting) {

                }

                override fun postClick(post: Posting) {
                    navigate(
                        MainFragmentDirections.actionMainFragmentToPostDetailFragment(post)
                    )
                }

            })
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RightSpaceItemDecoration(12.dpToPx(context)))
        }
    }

    private fun initDotsIndicator() {
        binding.indicatorDots.attachTo(binding.vpWeeklyCalendar)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.constJoinedRunningPost -> {
                navigate(
                    MainFragmentDirections.actionMainFragmentToJoinedRunningFragment()
                )
            }

            binding.ivCalendar -> {
                val userId = RunnerBeApplication.mTokenPreference.getUserId()
                navigate(
                    MainFragmentDirections.actionMainFragmentToMonthlyCalendarFragment(
                        userId,
                        0
                    )
                )
            }

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