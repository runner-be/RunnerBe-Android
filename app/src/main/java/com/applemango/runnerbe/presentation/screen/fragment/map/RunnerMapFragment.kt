package com.applemango.runnerbe.presentation.screen.fragment.map

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.FragmentRunnerMapBinding
import com.applemango.runnerbe.presentation.model.NestedScrollableViewHelper
import com.applemango.runnerbe.presentation.model.PriorityFilterTag
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
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
import com.sothree.slidinguppanel.ScrollableViewHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RunnerBeApplication.instance.firebaseTokenUpdate()
        binding.vm = viewModel
        binding.postListLayout.vm = viewModel
        binding.postListLayout.mainVm = mainViewModel
        binding.postListLayout.fragment = this
        context?.let {
            binding.postListLayout.postRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isShowInfoDialog.emit(true)
        }
        binding.fragment = this
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        observeBind()
        binding.slideLayout.setScrollableViewHelper(NestedScrollableViewHelper(binding.postListLayout.bodyLayout))
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.isRefresh.collect { refresh() }
            }
            launch {
                mainViewModel.clickedPost.collect {
                    runCatching {
                        val index = viewModel.postList.indexOf(it)
                        if (index != -1) onClickMarker(markerList[index], it)
                        else refresh()
                    }
                }
            }
            launch {
                mainViewModel.bookmarkPost.collect {
                    val index = viewModel.postList.indexOf(it)
                    if (index != -1) viewModel.postList[index] = it.copy()
                }
            }
        }
    }

    fun refresh() {
        viewModel.postList.clear()
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        viewModel.getRunningList(if (userId > 0) userId else null, isRefresh = true)
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
        viewModel.postList.forEach { post ->
            runCatching {
                val lat = post.gatherLatitude?.toDouble()
                val lng = post.gatherLongitude?.toDouble()
                if (lat != null && lng != null) {
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
                }
            }.onFailure {
                it.printStackTrace()
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

    fun writeClickEvent() {
        checkAdditionalUserInfo {
            navigate(MainFragmentDirections.actionMainFragmentToRunningWriteFragment())
        }
    }

    fun filterClick() {
        val filter = viewModel.filterData.value
        navigate(
            MainFragmentDirections.actionMainFragmentToRunningFilterFragment(
                gender = filter.genderTag,
                job = filter.jobTag,
                minAge = filter.minAge,
                maxAge = filter.maxAge
            )
        )
    }

    fun filterRunningTagClick() {
        val tagList = listOf(
            SelectItemParameter(resources.getString(RunningTag.Before.getTagNameResource())) {
                viewModel.filterRunningTag.value = RunningTag.Before
            },
            SelectItemParameter(resources.getString(RunningTag.After.getTagNameResource())) {
                viewModel.filterRunningTag.value = RunningTag.After
            },
            SelectItemParameter(resources.getString(RunningTag.Holiday.getTagNameResource())) {
                viewModel.filterRunningTag.value = RunningTag.Holiday
            }
        )
        context?.let {
            SelectItemDialog.createShow(it, tagList)
        }
    }

    fun filterPriorityTagClick() {
        val tagList = listOf(
            SelectItemParameter(resources.getString(PriorityFilterTag.BY_DISTANCE.getTagNameResource())) {
                viewModel.filterPriorityTag.value = PriorityFilterTag.BY_DISTANCE
            },
            SelectItemParameter(resources.getString(PriorityFilterTag.NEWEST.getTagNameResource())) {
                viewModel.filterPriorityTag.value = PriorityFilterTag.NEWEST
            }
        )
        context?.let {
            SelectItemDialog.createShow(it, tagList)
        }
    }

    fun getNoSelectMapMarkerResource(posting: Posting?): Int {
        return if (posting?.whetherEnd == "Y") R.drawable.ic_map_marker_whether_end
        else R.drawable.ic_map_marker
    }

    fun getSelectMapMarkerResource(posting: Posting?): Int {
        return if (posting?.whetherEnd == "Y") R.drawable.ic_select_map_marker_whether_end_no_profile
        else R.drawable.ic_select_map_marker_no_profile
    }
}