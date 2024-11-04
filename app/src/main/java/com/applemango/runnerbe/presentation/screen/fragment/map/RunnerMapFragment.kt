package com.applemango.runnerbe.presentation.screen.fragment.map

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.FragmentRunnerMapBinding
import com.applemango.runnerbe.presentation.model.NestedScrollableViewHelper
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.PostCalledFrom
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.AddressUtil
import com.applemango.runnerbe.util.ToastUtil
import com.applemango.runnerbe.util.setHeight
import com.jakewharton.rxbinding4.view.clicks
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.IllegalArgumentException

@AndroidEntryPoint
class RunnerMapFragment : BaseFragment<FragmentRunnerMapBinding>(R.layout.fragment_runner_map),
    OnMapReadyCallback {
    var userId = -1
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var mNaverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val markerList: ArrayList<Marker> = arrayListOf()
    private var clickedMarker: Marker? = null

    private val viewModel: RunnerMapViewModel by viewModels({ requireParentFragment() })
    private val mainViewModel: MainViewModel by viewModels({ requireParentFragment() })

    @Inject
    lateinit var postAdapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RunnerBeApplication.instance.firebaseTokenUpdate()
        binding.vm = viewModel
        binding.postListLayout.vm = viewModel
        binding.postListLayout.mainVm = mainViewModel
        context?.let {
            binding.postListLayout.postRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isShowInfoDialog.emit(true)
        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        observeBind()
        binding.slideLayout.setScrollableViewHelper(NestedScrollableViewHelper(binding.postListLayout.bodyLayout))
        setOnPostCreatedListener()
        binding.postListLayout.bodyLayout.setOnScrollChangeListener { v, _, _, _, _ ->
            if(!v.canScrollVertically(1) && !viewModel.isEndPage) {
                viewModel.getRunningList(if (userId > 0) userId else null, isRefresh = false)
            }
        }
        initPostRecyclerView()
        initListeners()
        setupPostFlow()
    }

    private fun setOnPostCreatedListener() {
        activity?.supportFragmentManager?.setFragmentResultListener("postCreated", viewLifecycleOwner) { _, result ->
            val refresh = result.getBoolean("refresh", false)
            if (refresh) {
                viewModel.getRunningList(if (userId > 0) userId else null, isRefresh = true)
            }
        }
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                mainViewModel.bookmarkPost.collect { posting ->
                    postAdapter.updatePostBookmark(posting)
                }
            }
            launch {
                mainViewModel.clickedPost.collect {
                    runCatching {
                        val index = viewModel.postList.value.indexOf(it)
                        if (index != -1) onClickMarker(markerList[index], it)
                        else return@collect
                    }
                }
            }
            launch {
                viewModel.actions.collect {
                    when(it) {
                        is RunnerMapViewModel.RunnerMapAction.MoveToWrite -> {
                            checkAdditionalUserInfo {
                                if(RunnerBeApplication.mTokenPreference.getMyRunningPace().isNullOrBlank()) {
                                    navigate(MainFragmentDirections.actionMainFragmentToPaceInfoFragment("map"))
                                } else navigate(MainFragmentDirections.actionMainFragmentToRunningWriteFragment(null))
                            }
                        }
                        is RunnerMapViewModel.RunnerMapAction.ShowSelectListDialog -> {
                            context?.let { context ->
                                SelectItemDialog.createShow(context, it.list)
                            }
                        }
                        is RunnerMapViewModel.RunnerMapAction.MoveToRunningFilter -> {
                            val filter = it.filterData
                            navigate(
                                MainFragmentDirections.actionMainFragmentToRunningFilterFragment(
                                    paces = filter.paceTags.toTypedArray(),
                                    gender = filter.genderTag,
                                    afterParty = filter.afterPartyTag,
                                    job = filter.jobTag,
                                    minAge = filter.minAge,
                                    maxAge = filter.maxAge
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.llMapRefresh.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    val userId = RunnerBeApplication.mTokenPreference.getUserId()
                    if (userId == -1) {
                        context?.let {
                            ToastUtil.showShortToast(it, "사용자 정보를 불러올 수 없어요. 약관에 동의해주세요")
                        }
                        return@subscribe
                    }

                    viewModel.refresh()
                },
            binding.topTxt.clicks()
                .window(3)
                .flatMapSingle { it.toList() }
                .filter { it.size >= 3 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        // TODO 테스트 용도이므로 기능 테스트 종료 시 삭제
                        val isTestMode = RunnerBeApplication.mTokenPreference.getIsTestMode()
                        val dialogMessage = if (!isTestMode)
                            "테스트 모드를 활성화하시겠어요?\n\n1. 이후 작성하는 게시글의 경도/위도/장소 데이터가 고정된 값으로 적용됩니다.\n2. 러닝 목록이 (용인시 동천동) 데이터로 고정됩니다."
                        else "테스트 모드를 비활성화하시겠어요?"

                        context?.let {
                            TwoButtonDialog.createShow(
                                it,
                                title = dialogMessage,
                                firstButtonText = resources.getString(R.string.no),
                                secondButtonText = resources.getString(R.string.yes),
                                firstEvent = {},
                                secondEvent = {
                                    RunnerBeApplication.mTokenPreference.setIsTestMode(!isTestMode)
                                    viewModel.refresh()
                                }
                            )
                        }
                    },
                    { throwable ->
                        throwable.printStackTrace()
                    }
                )
        )
    }

    private fun setupPostFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postList.collectLatest { postList ->
                    postAdapter.submitList(postList)
                }
            }
        }
    }

    private fun initPostRecyclerView() {
        binding.postListLayout.postRecyclerView.apply {
            adapter = postAdapter.apply {
                setPostFrom(PostCalledFrom.HOME)
                setPostClickListener(object: JoinedRunningClickListener {
                    override fun logWriteClick(post: Posting) {
                    }

                    override fun attendanceSeeClick(post: Posting) {
                    }

                    override fun attendanceManageClick(post: Posting) {
                    }

                    override fun bookMarkClick(post: Posting) {
                        mainViewModel.bookmarkStatusChange(post) // 북마크 화면에 추가/제거할 아이템 설정
                        viewModel.updatePostBookmark(post) // 게시글 화면에 해당 아이템 북마크 상태 변경
                    }

                    override fun postClick(post: Posting) {
                        navigate(
                            MainFragmentDirections.actionMainFragmentToPostDetailFragment(post)
                        )
                    }

                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
        }
    }

    override fun onMapReady(map: NaverMap) {
        mNaverMap = map
        mNaverMap.locationSource = locationSource
        mNaverMap.mapType = NaverMap.MapType.Navi
        mNaverMap.isNightModeEnabled = true
        mNaverMap.locationTrackingMode = LocationTrackingMode.Follow
        //SlidingUpPanelLayout이 크기를 자꾸 변경하는 문제가 있어서 레이아웃 사이즈를 초기에 고정시켜버리기
        binding.mapLayout.setHeight(binding.mapLayout.measuredHeight)
        //현재위치로 주소 디폴트 셋팅
        // TODO 테스트 용도이므로 완료 시 삭제
        binding.topTxt.text = if (RunnerBeApplication.mTokenPreference.getIsTestMode()) {
            val testData = LatLng(37.3419817, 127.0940174)
            AddressUtil.getAddress(
                requireContext(),
                testData.latitude,
                testData.longitude
            )
        } else {
            AddressUtil.getAddress(
                requireContext(),
                mNaverMap.cameraPosition.target.latitude,
                mNaverMap.cameraPosition.target.longitude
            )
        }

        //위치가 바뀔 때마다 주소 업데이트
        mNaverMap.addOnLocationChangeListener { location ->
            binding.topTxt.run {
                text =
                    AddressUtil.getAddress(requireContext(), location.latitude, location.longitude)
            }
        }

        mNaverMap.addOnCameraChangeListener { _, _ ->
            val center = LatLng(
                mNaverMap.cameraPosition.target.latitude,
                mNaverMap.cameraPosition.target.longitude
            )
            viewModel.refreshThisLocation.value =
                mNaverMap.locationTrackingMode != LocationTrackingMode.Follow
            viewModel.coordinator = center
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listUpdateUiState.collect {
                context?.let { context ->
                    if (it is UiState.Loading) showLoadingDialog(context)
                    else dismissLoadingDialog()
                }
                when (it) {
                    is UiState.Success -> {
                        markerUpdate()
                    }

                    else -> {
                        Log.e(this.javaClass.name, "onMapReady - when - else - UiState")
                    }
                }
            }
        }
    }

    private fun markerClear() {
        markerList.forEach { marker -> marker.map = null }
        clickedMarker = null
        markerList.clear()
    }

    private fun markerUpdate() {
        markerClear()
        viewModel.postList.value.forEach { post ->
            try {
                val lat = requireNotNull(post.gatherLatitude?.toDouble())
                val lng = requireNotNull(post.gatherLongitude?.toDouble())

                val marker = Marker().apply {
                    position = LatLng(lat, lng)
                    map = mNaverMap
                    icon = OverlayImage.fromResource(getNoSelectMapMarkerResource(post))
                    setOnClickListener {
                        mainViewModel.clickPost(post)
                        true
                    }
                }
                markerList.add(marker)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    private fun onClickMarker(overlay: Marker, posting: Posting?) {
        if (overlay == clickedMarker) {
            overlay.icon = OverlayImage.fromResource(getNoSelectMapMarkerResource(posting))
            clickedMarker = null
        } else {
            clickedMarker?.icon = OverlayImage.fromResource(
                getNoSelectMapMarkerResource(mainViewModel.clickedPost.value)
            )
            overlay.icon = OverlayImage.fromResource(getSelectMapMarkerResource(posting))
            clickedMarker = overlay
        }
    }

    private fun getNoSelectMapMarkerResource(posting: Posting?): Int {
        return if (posting?.whetherEnd == "Y") R.drawable.ic_map_marker_whether_end
        else R.drawable.ic_map_marker
    }

    private fun getSelectMapMarkerResource(posting: Posting?): Int {
        return if (posting?.whetherEnd == "Y") R.drawable.ic_select_map_marker_whether_end_no_profile
        else R.drawable.ic_select_map_marker_no_profile
    }
}