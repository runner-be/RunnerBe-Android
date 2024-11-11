package com.applemango.runnerbe.presentation.screen.fragment.map.write

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.vo.RunningWriteTransferData
import com.applemango.runnerbe.databinding.FragmentRunningWriteBinding
import com.applemango.runnerbe.presentation.model.DateResultListener
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.model.TimeResultListener
import com.applemango.runnerbe.presentation.screen.activity.AddressActivity
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateTimePickerDialog
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectData
import com.applemango.runnerbe.presentation.screen.dialog.timeselect.TimeSelectPickerDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.ToastUtil
import com.jakewharton.rxbinding4.view.clicks
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author niaka
 */
class RunningWriteOneFragment :
    BaseFragment<FragmentRunningWriteBinding>(R.layout.fragment_running_write) {

    private lateinit var locationSource: FusedLocationSource

    private val viewModel: RunningWriteOneViewModel by viewModels()
    private lateinit var addressLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
        binding.scrollView.requestDisallowInterceptTouchEvent(true)
        initListeners()
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
                viewModel.updateAddress(it)
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

    //이 부분은 추후 리팩토링이 들어가야 할 듯 합니다.
    private fun initListeners() {
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
                        if (isSelectedTimeExpired()) {
                            context?.let {
                                ToastUtil.showShortToast(it, "선택된 시간이 올바르지 않아요")
                            }
                            return@subscribe
                        }

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
                                    },
                                    placeData = viewModel.runningPlaceInfo.value
                                )
                            )
                        )
                    },{
                        it.printStackTrace()
                    }),
            )
        }
    }

    private fun isSelectedTimeExpired(): Boolean {
        val calendar = Calendar.getInstance()
        val currentDateTime = calendar.time

        val displayedData = viewModel.runningDisplayDate.value
        val date = displayedData.formatDate
        val hour: String = displayedData.hour
        val amPm: String = displayedData.AMAndPM

        val displayedCalendar = Calendar.getInstance().apply {
            val dateFormat = SimpleDateFormat("M/d (E)", Locale.getDefault())
            time = dateFormat.parse(date) ?: return false

            if (amPm == "PM" && hour.toInt() != 12) {
                set(Calendar.HOUR_OF_DAY, hour.toInt() + 12)
            } else if (amPm == "AM" && hour.toInt() == 12) {
                set(Calendar.HOUR_OF_DAY, 0)
            } else {
                set(Calendar.HOUR_OF_DAY, hour.toInt())
            }

            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return displayedCalendar.time.before(currentDateTime)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}