package com.applemango.runnerbe.presentation.screen.fragment.map.write

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.vo.RunningWriteTransferData
import com.applemango.runnerbe.databinding.FragmentRunningWriteBinding
import com.applemango.runnerbe.databinding.ItemMapInfoBinding
import com.applemango.runnerbe.presentation.model.DateResultListener
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.model.TimeResultListener
import com.applemango.runnerbe.presentation.screen.activity.AddressActivity
import com.applemango.runnerbe.presentation.screen.activity.HomeActivity
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateTimePickerDialog
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectData
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectPickerDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.AddressUtil
import com.jakewharton.rxbinding4.view.clicks
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
import java.util.concurrent.TimeUnit

/**
 * @author niaka
 *
 */
class RunningWriteOneFragment :
    BaseFragment<FragmentRunningWriteBinding>(R.layout.fragment_running_write),
    OnMapReadyCallback {

    private val navArgs: RunningWriteOneFragmentArgs by navArgs()

    private var centerMarker: Marker? = null
    private var markerInfoView: InfoWindow = InfoWindow()

    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var mNaverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val viewModel: RunningWriteOneViewModel by activityViewModels()
    private lateinit var addressLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        binding.scrollView.requestDisallowInterceptTouchEvent(true)
        setupListeners()
        addressLauncher = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                processUserLocation(result)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navPopStack(R.id.mainFragment)
                }
            })
        observeBind()
    }

    private fun processUserLocation(result: ActivityResult) {
        result.data?.let { data ->
            val address = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.getParcelableExtra("address", AddressData::class.java)
            } else {
                data.getParcelableExtra("address")
            }
            address?.let {
                viewModel.updateSelectedLocation(it)
                viewModel.updateCoordinate(it)
            }
        }
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

    private fun setupListeners() {
        with(binding) {
            compositeDisposable.addAll(
                dateLayout.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        DateTimePickerDialog.createShow(
                            requireContext(),
                            displayDate = viewModel.runningDisplayDate.value,
                            resultListener = object : DateResultListener {
                                override fun getDate(date: Date, displayDate: DateSelectData) {
                                    viewModel.runningDate = date
                                    viewModel.runningDisplayDate.value = displayDate
                                }
                            })
                    },
                timeLayout.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        TimeSelectPickerDialog.createShow(
                            requireContext(),
                            selectedData = viewModel.runningDisplayTime.value,
                            resultListener = object : TimeResultListener {
                                override fun getDate(displayTime: TimeSelectData) {
                                    viewModel.runningDisplayTime.value = displayTime
                                }
                            }
                        )
                    },
                locationLayout.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val intent = Intent(context, AddressActivity::class.java)
                        addressLauncher.launch(intent)
                    },
                backBtn.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        navPopStack()
                    },
                nextButton.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe ({
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
                    },{
                        it.printStackTrace()
                    }),
            )
        }
    }
}