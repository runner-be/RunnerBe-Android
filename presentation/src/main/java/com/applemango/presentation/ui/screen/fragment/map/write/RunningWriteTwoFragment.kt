package com.applemango.presentation.ui.screen.fragment.map.write

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.applemango.presentation.ui.screen.deco.RecyclerViewItemDeco
import com.applemango.presentation.ui.screen.dialog.message.MessageDialog
import com.applemango.presentation.ui.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.state.UiState
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentRunningWriteTwoBinding
import com.google.android.material.slider.RangeSlider.OnChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author niaka
 *
 */
@AndroidEntryPoint
class RunningWriteTwoFragment :
    BaseFragment<FragmentRunningWriteTwoBinding>(R.layout.fragment_running_write_two)
    ,View.OnClickListener {

    private val viewModel: RunningWriteTwoViewModel by viewModels()
    private val args : RunningWriteTwoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.oneData.value = args.data

        binding.vm = viewModel
        binding.backBtn.setOnClickListener(this)
        binding.ageSlider.addOnChangeListener(OnChangeListener { slider, _, _ ->
            val ages = slider.values
            viewModel.recruitmentStartAge.value = ages[0].toInt()
            viewModel.recruitmentEndAge.value = ages[1].toInt()
        })
        context?.let {
            binding.paceLevelSelectRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 8))
        }

        binding.postButton.setOnClickListener(this)
        observeBind()
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.writeState.collect {
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
                            resources.getString(R.string.complete_running_write),
                            Toast.LENGTH_SHORT
                        ).show()
                        activity?.supportFragmentManager?.setFragmentResult("postListUpdate", bundleOf("refresh" to true))
                        findNavController().popBackStack(R.id.mainFragment, false)
                    }

                    is UiState.Loading -> {

                    }

                    is UiState.Empty -> {

                    }

                    is UiState.AnonymousFailed -> {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

//    override fun onMapReady(map: NaverMap) {
//        mNaverMap = map
//        mNaverMap.mapType = NaverMap.MapType.Navi
//        mNaverMap.isNightModeEnabled = true
//        mNaverMap.uiSettings.isScrollGesturesEnabled = false
//        mNaverMap.uiSettings.isZoomControlEnabled = false
//        createCenterMarker()
//    }

//    private fun createCenterMarker() {
//        centerMarker = Marker()
//        val lat = viewModel.oneData.value.coordinate.latitude
//        val lng = viewModel.oneData.value.coordinate.longitude
//        centerMarker?.apply {
//            position = LatLng(lat, lng)
//            map = mNaverMap
//            icon = OverlayImage.fromResource(R.drawable.ic_select_map_marker_no_profile)
//        }
//
//        mNaverMap.moveCamera(CameraUpdate.scrollTo(viewModel.oneData.value.coordinate))
//        context?.let {
//            viewModel.locationInfo.value = AddressUtil.getAddress(it, lat, lng)
//        }
//    }

    override fun onClick(v: View?) {
        when(v) {
            binding.backBtn -> navPopStack()
            binding.postButton -> {
                context?.let {
                    TwoButtonDialog.createShow(
                        it,
                        title = resources.getString(R.string.question_running_write),
                        firstButtonText = resources.getString(R.string.no),
                        secondButtonText = resources.getString(R.string.yes),
                        firstEvent = {},
                        secondEvent = {
                            viewModel.writeRunning()
                        }
                    )
                }
            }
        }
    }
}