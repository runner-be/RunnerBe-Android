package com.applemango.presentation.ui.screen.fragment.map

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.type.PostCalledFrom
import com.applemango.presentation.ui.screen.deco.RecyclerViewItemDeco
import com.applemango.presentation.ui.screen.dialog.selectitem.SelectItemDialog
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.screen.fragment.main.MainViewModel
import com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.presentation.ui.state.UiState
import com.applemango.presentation.util.AddressUtil
import com.applemango.presentation.util.NestedScrollableViewHelper
import com.applemango.presentation.util.ToastUtil
import com.applemango.presentation.util.setHeight
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentRunnerMapBinding
import com.applemango.presentation.ui.screen.fragment.main.MainFragmentDirections
import com.jakewharton.rxbinding4.view.clicks
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class RunnerMapFragment : BaseFragment<FragmentRunnerMapBinding>(R.layout.fragment_runner_map),
    OnMapReadyCallback {
    private lateinit var mNaverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val markerList: ArrayList<Marker> = arrayListOf()
    private var clickedMarker: Marker? = null

    private val viewModel: RunnerMapViewModel by viewModels({ requireParentFragment() })
    private val mainViewModel: MainViewModel by viewModels({ requireParentFragment() })

    @Inject
    lateinit var postAdapter: PostAdapter

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.runnerMapViewModel = viewModel
        binding.postListLayout.vm = viewModel
        binding.postListLayout.mainVm = mainViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isShowInfoDialog.emit(true)
        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        observeBind()
        binding.slideLayout.setScrollableViewHelper(NestedScrollableViewHelper(binding.postListLayout.bodyLayout))
        setOnPostListUpdateListener()

        initPostRecyclerView()
        initSlideLayoutListener()
        initListeners()
        setupPostFlow()
//        viewModel.updateFirebaseToken() 원래 여기서도 Jwt 토큰 업데이트 해야하는건지
        viewModel.getUserId()
        viewModel.getUserPace()
    }

    private fun setOnPostListUpdateListener() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            "postListUpdate",
            viewLifecycleOwner
        ) { _, result ->
            val refresh = result.getBoolean("refresh", false)
            if (refresh) {
                viewModel.getRunningList(isRefresh = true)
            }
        }
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
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
                    when (it) {
                        is RunnerMapViewModel.RunnerMapAction.MoveToWrite -> {
                            checkAdditionalUserInfo(viewModel.userId.value) {
                                if (viewModel.userPace.value != null) {
                                    navigate(
                                        MainFragmentDirections.actionMainFragmentToPaceInfoFragment(
                                            "map"
                                        )
                                    )
                                } else navigate(
                                    MainFragmentDirections.actionMainFragmentToRunningWriteFragment(
                                        null
                                    )
                                )
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
        viewModel.getRunningList(isRefresh = false)
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

    private fun initSlideLayoutListener() {
        binding.slideLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                viewModel.panelTop.value = panel?.top ?: 0
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: PanelState?,
                newState: PanelState?
            ) {
                when (newState) {
                    PanelState.EXPANDED -> {
                        binding.postListLayout.postRecyclerView.isNestedScrollingEnabled = true
                    }

                    else -> {

                    }
                }
            }
        })
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.ivNotification.clicks()
                .subscribe {
                    navigate(
                        MainFragmentDirections
                            .actionMainFragmentToAlarmFragment()
                    )
                },
            binding.ivCurrentLocation.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    moveToCurrentLocation(binding.slideLayout.panelState)
                },
            binding.llMapRefresh.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (viewModel.userId.value == -1) {
                        context?.let {
                            ToastUtil.showShortToast(it, "사용자 정보를 불러올 수 없어요. 약관에 동의해주세요")
                        }
                        return@subscribe
                    }

                    viewModel.refresh()
                },
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
                setPostClickListener(object : JoinedRunningClickListener {
                    override fun logWriteClick(post: PostingModel) {
                    }

                    override fun attendanceSeeClick(post: PostingModel) {
                    }

                    override fun attendanceManageClick(post: PostingModel) {
                    }

                    override fun bookMarkClick(post: PostingModel) {
                        mainViewModel.bookmarkStatusChange(post) // 북마크 화면에 추가/제거할 아이템 설정
                        viewModel.updatePostBookmark(post) // 게시글 화면에 해당 아이템 북마크 상태 변경
                    }

                    override fun postClick(post: PostingModel) {
                        navigate(
                            MainFragmentDirections.actionMainFragmentToPostDetailFragment(post)
                        )
                    }

                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewItemDeco(context, 12))
            itemAnimator = null

            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return false
                }

                override fun onTouchEvent(recyclerView: RecyclerView, e: MotionEvent) {
                    if (binding.slideLayout.panelState != PanelState.EXPANDED) {
                        recyclerView.onInterceptTouchEvent(e)
                        binding.slideLayout.post {
                            binding.slideLayout.panelState = PanelState.EXPANDED
                        }
                    }
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }

            })

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (binding.slideLayout.panelState != PanelState.EXPANDED) {
                            binding.slideLayout.post {
                                binding.slideLayout.panelState = PanelState.EXPANDED
                            }
                            recyclerView.stopScroll()
                        }
                    }
                }
            })
        }
    }

    private fun moveToCurrentLocation(panelState: PanelState) {
        val location = locationSource.lastLocation
        if (location != null) {
            val currentLatLng = LatLng(location.latitude, location.longitude)

            val cameraUpdate = when (panelState) {
                PanelState.COLLAPSED -> {
                    CameraUpdate.scrollTo(currentLatLng)
                        .animate(CameraAnimation.Easing)
                }

                PanelState.ANCHORED -> {
                    val bottomDrawerHeight = binding.bottomDrawerLayout.height
                    val mapHeight = binding.mapView.height
                    val visibleMapHeight = mapHeight - bottomDrawerHeight

                    val adjustmentRatio = visibleMapHeight / 2

                    val screenPosition = mNaverMap.projection.toScreenLocation(currentLatLng)
                    screenPosition.y += adjustmentRatio.toFloat()

                    val adjustedLatLng = mNaverMap.projection.fromScreenLocation(screenPosition)

                    CameraUpdate.scrollTo(adjustedLatLng)
                        .animate(CameraAnimation.Easing)
                }

                PanelState.EXPANDED -> {
                    val bottomLayoutHeight = binding.bottomDrawerLayout.height
                    val bottomTabLayoutHeight = binding.bottomDrawerTabLayout.height
                    val mapHeight = binding.mapLayout.height

                    val adjustmentRatio =
                        ((bottomLayoutHeight - bottomTabLayoutHeight).toFloat() / 2) / mapHeight

                    val screenPosition = mNaverMap.projection.toScreenLocation(currentLatLng)
                    screenPosition.y -= (mapHeight * adjustmentRatio).toInt()

                    val adjustedLatLng = mNaverMap.projection.fromScreenLocation(screenPosition)

                    CameraUpdate.scrollTo(adjustedLatLng)
                        .animate(CameraAnimation.Easing)
                }

                else -> null
            }

            cameraUpdate?.let { mNaverMap.moveCamera(it) }
        } else {
            context?.let {
                ToastUtil.showShortToast(
                    it,
                    getString(R.string.toast_error_move_to_current_location)
                )
            }
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
        // 현재위치로 주소 디폴트 셋팅
        binding.topTxt.text = AddressUtil.getAddress(
            requireContext(),
            mNaverMap.cameraPosition.target.latitude,
            mNaverMap.cameraPosition.target.longitude
        )

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
                (mNaverMap.locationTrackingMode != LocationTrackingMode.Follow)
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

    private fun onClickMarker(overlay: Marker, posting: PostingModel?) {
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

    private fun getNoSelectMapMarkerResource(posting: PostingModel?): Int {
        return if (posting?.whetherEnd == "Y") R.drawable.ic_map_marker_whether_end
        else R.drawable.ic_map_marker
    }

    private fun getSelectMapMarkerResource(posting: PostingModel?): Int {
        return if (posting?.whetherEnd == "Y") R.drawable.ic_select_map_marker_whether_end_no_profile
        else R.drawable.ic_select_map_marker_no_profile
    }
}