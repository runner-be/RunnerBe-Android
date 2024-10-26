package com.applemango.runnerbe.presentation.screen.fragment.map

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.FragmentRunnerMapBinding
import com.applemango.runnerbe.presentation.model.NestedScrollableViewHelper
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.PostCalledFrom
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.AddressUtil
import com.applemango.runnerbe.util.setHeight
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject


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
        binding.postListLayout.bodyLayout.setOnScrollChangeListener { v, _, _, _, _ ->
            if(!v.canScrollVertically(1) && !viewModel.isEndPage) {
                viewModel.getRunningList(if (userId > 0) userId else null, isRefresh = false)
            }
        }
        initPostRecyclerView()
        setupPostFlow()
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

    private fun setupPostFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postList.collectLatest { postList ->
                postAdapter.submitList(postList)
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
        binding.topTxt.text = AddressUtil.getAddress(
            requireContext(),
            mNaverMap.cameraPosition.target.latitude,
            mNaverMap.cameraPosition.target.longitude
        )

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