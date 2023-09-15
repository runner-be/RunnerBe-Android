package com.applemango.runnerbe.presentation.screen.fragment.main.postdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentPostDetailBinding
import com.applemango.runnerbe.databinding.ItemMapInfoBinding
import com.applemango.runnerbe.presentation.model.listener.PostDialogListener
import com.applemango.runnerbe.presentation.screen.dialog.appliedrunner.WaitingRunnerListDialog
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.AddressUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment :
    OnMapReadyCallback,
    BaseFragment<FragmentPostDetailBinding>(R.layout.fragment_post_detail) {

    private val viewModel: PostDetailViewModel by viewModels()
    private val args: PostDetailFragmentArgs by navArgs()

    private var centerMarker: Marker? = null
    private var markerInfoView: InfoWindow = InfoWindow()

    private lateinit var mNaverMap: NaverMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.post.value = args.posting
        binding.fragment = this
        observeBind()
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        refresh()
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


    fun refresh() {
        viewModel.post.value?.let {
            viewModel.getPostDetail(it.postId, RunnerBeApplication.mTokenPreference.getUserId())
        }
    }

    fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.processUiState.collect {
                    context?.let { context ->
                        if (it is UiState.Loading) showLoadingDialog(context)
                        else dismissLoadingDialog()
                    }
                    when (it) {
                        is UiState.Success -> {
                            Toast.makeText(
                                context,
                                resources.getString(if (viewModel.isMyPost()) R.string.msg_post_close else R.string.msg_post_apply),
                                Toast.LENGTH_SHORT
                            ).show()
                            refresh()
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
                    }
                }
            }

            launch {
                viewModel.reportUiState.collect {
                    context?.let { context ->
                        if (it is UiState.Loading) showLoadingDialog(context)
                        else dismissLoadingDialog()
                    }
                    when (it) {
                        is UiState.Success -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.report_complete),
                                Toast.LENGTH_SHORT
                            ).show()
                            navPopStack()
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
                        is UiState.NetworkError -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.error_re_start),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            launch {
                viewModel.dropUiState.collect {
                    context?.let { context ->
                        if (it is UiState.Loading) showLoadingDialog(context)
                        else dismissLoadingDialog()
                    }
                    when (it) {
                        is UiState.Success -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.delete_complete),
                                Toast.LENGTH_SHORT
                            ).show()
                            navPopStack()
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
                        is UiState.NetworkError -> {
                            Toast.makeText(context, "문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun moveToMessage() {
        viewModel.roomId?.let { roomId ->
            viewModel.post.value?.nickName?.let { nickName ->
                navigate(
                    PostDetailFragmentDirections.actionPostDetailFragmentToRunningTalkDetailFragment(
                        roomId, nickName
                    )
                )
            }
        }
    }

    override fun onMapReady(map: NaverMap) {
        mNaverMap = map
        mNaverMap.mapType = NaverMap.MapType.Navi
        mNaverMap.isNightModeEnabled = true
        createCenterMarker()
        initMarkerInfoView()
        mNaverMap.uiSettings.isScrollGesturesEnabled = false
    }

    private fun createCenterMarker() {
        centerMarker = Marker()
        val lat = getLatitude()
        val lng = getLongitude()
        centerMarker?.apply {
            position = LatLng(lat, lng)
            map = mNaverMap
            icon = OverlayImage.fromResource(R.drawable.ic_select_map_marker_no_profile_mini)
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

        mNaverMap.moveCamera(CameraUpdate.scrollTo(LatLng(lat, lng)))
        context?.let {
            viewModel.locationInfo.value = AddressUtil.getAddress(it, lat, lng)
        }
    }

    private fun initMarkerInfoView() {
        markerInfoView.adapter = object : InfoWindow.DefaultViewAdapter(requireContext()) {
            override fun getContentView(p0: InfoWindow): View {
                val view: ItemMapInfoBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.item_map_info, null, false)
                val latitude = getLatitude()
                val longitude = getLongitude()
                val address = AddressUtil.getAddressSimpleLine(context, latitude, longitude)
                val addressLine = AddressUtil.getAddressSimpleLine(context, latitude, longitude)
                    .split(" ")
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

    fun showDropDialog() {
        val dialogList = listOf(
            SelectItemParameter(resources.getString(R.string.do_delete)) {
                viewModel.dropPost()
            }
        )
        context?.let { SelectItemDialog.createShow(it, dialogList) }

    }

    fun clickBottomButton() {
        context?.let {
            TwoButtonDialog.createShow(
                context = it,
                title = resources.getString(if (viewModel.isMyPost()) R.string.question_post_close else R.string.question_post_apply),
                firstButtonText = resources.getString(R.string.no),
                secondButtonText = resources.getString(R.string.yes),
                firstEvent = {},
                secondEvent = {
                    viewModel.bottomProcess()
                }
            )
        }
    }

    fun showAppliedRunnerListDialog() {
        WaitingRunnerListDialog(viewModel.waitingInfo, viewModel, object : PostDialogListener {
            override fun moveToMessage(roomId: Int, repUserName: String?) {
                moveToMessage()
            }

            override fun dismiss() {}
        }, viewModel.roomId).show(childFragmentManager, "appliedRunner")
    }

    private fun getLatitude(): Double = try {
        viewModel.post.value?.gatherLatitude?.toDouble()!!
    } catch (e: Exception) {
        mNaverMap.cameraPosition.target.latitude
    }

    private fun getLongitude(): Double = try {
        viewModel.post.value?.gatherLongitude?.toDouble()!!
    } catch (e: Exception) {
        mNaverMap.cameraPosition.target.longitude
    }

    fun showReportDialog() {
        context?.let {
            TwoButtonDialog.createShow(
                it,
                title = resources.getString(R.string.msg_warning_report),
                firstButtonText = resources.getString(R.string.yes),
                secondButtonText = resources.getString(R.string.no),
                firstEvent = {
                    viewModel.reportPost()
                },
                secondEvent = {}
            )
        }
    }
}