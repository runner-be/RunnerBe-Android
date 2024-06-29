package com.applemango.runnerbe.presentation.screen.fragment.map.write

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.vo.RunningWriteTransferData
import com.applemango.runnerbe.databinding.FragmentRunningWriteBinding
import com.applemango.runnerbe.databinding.ItemMapInfoBinding
import com.applemango.runnerbe.presentation.model.DateResultListener
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.model.TimeResultListener
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateTimePickerDialog
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectData
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectPickerDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.AddressUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author niaka
 *
 */
class RunningWriteOneFragment :
    BaseFragment<FragmentRunningWriteBinding>(R.layout.fragment_running_write),
    OnMapReadyCallback, View.OnClickListener {

    private var centerMarker: Marker? = null
    private var markerInfoView: InfoWindow = InfoWindow()

    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var mNaverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val viewModel: RunningWriteOneViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        binding.mapView.getMapAsync(this)
        binding.scrollView.requestDisallowInterceptTouchEvent(true)
        binding.mapView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                }
            }
            v.performClick()
            binding.mapView.onTouchEvent(event)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navPopStack(R.id.mainFragment)
                }
            })
        observeBind()
        binding.dateLayout.setOnClickListener(this)
        binding.timeLayout.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.nextButton.setOnClickListener(this)
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.radioChecked.collect {
                val tag = when (it) {
                    R.id.afterTab -> RunningTag.After
                    R.id.holidayTab -> RunningTag.Holiday
                    else -> RunningTag.Before
                }
                viewModel.runningDisplayDate.emit(DateSelectData.runningTagDefault(tag))
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


    override fun onMapReady(map: NaverMap) {
        mNaverMap = map
        createCenterMarker()
        initMarkerInfoView()
        mNaverMap.mapType = NaverMap.MapType.Navi
        mNaverMap.isNightModeEnabled = true
        mNaverMap.locationSource = locationSource
        mNaverMap.locationTrackingMode = LocationTrackingMode.Follow

        mNaverMap.addOnCameraChangeListener { _, _ ->
            if (centerMarker?.infoWindow != null) {
                markerInfoView.close()
            }
            val center = LatLng(
                mNaverMap.cameraPosition.target.latitude,
                mNaverMap.cameraPosition.target.longitude
            )
            viewModel.coordinate = center
            centerMarker?.position = center
        }
    }

    private fun createCenterMarker() {
        centerMarker = Marker()
        centerMarker?.apply {
            position = LatLng(
                mNaverMap.cameraPosition.target.latitude,
                mNaverMap.cameraPosition.target.longitude
            )
            map = mNaverMap
            icon = OverlayImage.fromResource(R.drawable.ic_select_map_marker_no_profile)
            this.setOnClickListener {
                val marker = it as Marker
                if (marker.infoWindow == null) {
                    openAddressView(marker)
                } else {
                    markerInfoView.close()
                }
                true
            }
        }
        mNaverMap.setOnMapClickListener { _, _ ->
            markerInfoView.close()
        }
    }

    //이 부분은 추후 리팩토링이 들어가야 할 듯 합니다.
    private fun initMarkerInfoView() {
        markerInfoView.adapter = object : InfoWindow.DefaultViewAdapter(requireContext()) {
            override fun getContentView(p0: InfoWindow): View {
                val view: ItemMapInfoBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.item_map_info, null, false)
                val address = AddressUtil.getAddressSimpleLine(
                    context,
                    viewModel.coordinate.latitude,
                    viewModel.coordinate.longitude
                )
                val addressLine = AddressUtil.getAddressSimpleLine(
                    context,
                    viewModel.coordinate.latitude,
                    viewModel.coordinate.longitude
                ).split(" ")
                if (addressLine.size > 1) {
                    view.oneTextView.text =
                        addressLine.filterIndexed { index, _ -> index != 0 && index <= addressLine.size / 2 }
                            .joinToString(separator = " ")
                    view.twoTextView.text =
                        addressLine.filterIndexed { index, _ -> index > addressLine.size / 2 }
                            .joinToString(separator = " ")
                } else {
                    view.oneTextView.text = address
                }

                return view.root
            }
        }
    }

    private fun openAddressView(marker: Marker) {
        markerInfoView.open(marker)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.dateLayout -> {
                DateTimePickerDialog.createShow(
                    requireContext(),
                    displayDate = viewModel.runningDisplayDate.value,
                    resultListener = object : DateResultListener {
                        override fun getDate(date: Date, displayDate: DateSelectData) {
                            viewModel.runningDate = date
                            viewModel.runningDisplayDate.value = displayDate
                        }
                    })
            }
            binding.timeLayout -> {
                TimeSelectPickerDialog.createShow(
                    requireContext(),
                    selectedData = viewModel.runningDisplayTime.value,
                    resultListener = object : TimeResultListener {
                        override fun getDate(displayTime: TimeSelectData) {
                            viewModel.runningDisplayTime.value = displayTime
                        }
                    }
                )
            }
            binding.backBtn -> navPopStack()
            binding.nextButton -> {
                navigate(
                    RunningWriteOneFragmentDirections.actionRunningWriteFragmentToRunningWriteTwoFragment(
                        RunningWriteTransferData(
                            runningTitle = viewModel.runningTitle.value,
                            runningDate = viewModel.runningDate,
                            runningDisplayDate = viewModel.runningDisplayDate.value,
                            runningDisplayTime = viewModel.runningDisplayTime.value,
                            coordinate = viewModel.coordinate,
                            runningTag = when (viewModel.radioChecked.value) {
                                R.id.afterTab -> RunningTag.After
                                R.id.holidayTab -> RunningTag.Holiday
                                else -> RunningTag.Before
                            }
                        )
                    )
                )
            }
        }
    }

}