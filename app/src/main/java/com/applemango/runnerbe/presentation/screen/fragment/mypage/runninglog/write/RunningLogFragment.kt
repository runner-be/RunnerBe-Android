package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.write

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentRunningLogBinding
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.message.YesNoButtonDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherItem
import com.applemango.runnerbe.presentation.screen.dialog.weather.getWeatherItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.ToastUtil
import com.applemango.runnerbe.util.manager.SinglePhotoManager
import com.applemango.runnerbe.util.parseLocalDateToKorean
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.touches
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RunningLogFragment : BaseFragment<FragmentRunningLogBinding>(R.layout.fragment_running_log) {

    private val viewModel: RunningLogViewModel by viewModels()
    private val navArgs: RunningLogFragmentArgs by navArgs()

    private var _photoManager: SinglePhotoManager? = null
    private val photoManager get() = _photoManager!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val strDate = navArgs.date
        val logId = navArgs.logId
        val gatheringId = navArgs.gatheringId

        val logType = if (gatheringId != null) {
            RunningLogType.TEAM
        } else RunningLogType.ALONE

        viewModel.updateLogDate(parseLocalDateToKorean(LocalDate.parse(strDate)))
        viewModel.updateLogId(logId?.toIntOrNull())
        viewModel.updateGatheringId(gatheringId?.toIntOrNull())
        viewModel.updateLogType(logType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initListeners()
        setupPostedRunningLog()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    askAndDismissDialog()
                }
            })
        _photoManager = SinglePhotoManager(this) { croppedImage ->
            viewModel.updateLogImage(croppedImage)
        }
    }

    override fun onDestroyView() {
        _photoManager = null
        super.onDestroyView()
    }

    private fun askAndDismissDialog() {
        YesNoButtonDialog.createShow(
            requireContext(),
            "저장해야 로그가 기록돼요.\n정말 나가시겠어요?",
            "네",
            "아니오",
            positiveEvent = fun() {
                goBack()
            },
            negativeEvent = fun() {

            }
        )
    }

    private fun setupPostedRunningLog() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postedRunningLogFlow.collect { response ->
                    response?.let {
                        val log = response.result.runningLog
                        val stamp = getStampItemByCode(log.stampCode)
                        viewModel.updateLogDate(parseLocalDateToKorean(log.runnedDate.toLocalDate()))
                        stamp?.let {
                            viewModel.updateStamp(it)
                        }
                        viewModel.updateLogDiary(log.contents)
                        viewModel.updateDegreeAndWeather(
                            log.weatherDegree.toString(),
                            getWeatherItemByCode(log.weatherCode)
                        )
                        viewModel.updateLogVisibility(log.isOpened == 1)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        with(binding) {
            compositeDisposable.addAll(
                ivPhoto.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val imageUri = viewModel.logImage.value
                        imageUri?.let { uri ->
                            // TODO 추후에 사진 여러장 저장으로 수정
                            navigate(
                                RunningLogFragmentDirections
                                    .actionRunningLogFragmentToImageDetailFragment(
                                        "",
                                        arrayOf(uri.toString()),
                                    )
                            )
                        }
                    },
                flWeather.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        binding.constWeather.performClick()
                    },
                btnBack.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        askAndDismissDialog()
                    },
                constStamp.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val prevStampData = viewModel.logStamp.value
                        val prevStamp = if (prevStampData == StampItem.unavailableStampItem) {
                            StampItem(
                                "RUN001",
                                R.drawable.ic_stamp_1_personal,
                                requireContext().getString(R.string.stamp_1_name),
                                requireContext().getString(R.string.stamp_1_description),
                                true
                            )
                        } else {
                            prevStampData
                        }

                        StampBottomSheetDialog.createAndShow(
                            childFragmentManager,
                            prevStamp,
                            viewModel.gatheringId.value == null
                        ) { stamp ->
                            viewModel.updateStamp(stamp)
                        }
                    },
                constWeather.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val prevWeather = viewModel.logWeather.value
                        val prevDegree = viewModel.logDegree.value

                        WeatherBottomSheetDialog.createAndShow(
                            childFragmentManager,
                            prevWeather,
                            prevDegree ?: ""
                        ) { weather, temperature ->
                            Glide.with(requireContext())
                                .load(weather.image)
                                .into(binding.ivWeather)
                            val parsedDegree = parseTemperature(temperature)
                            viewModel.updateDegreeAndWeather(parsedDegree, weather)
                        }
                    },
                constDiary.touches()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        etDiary.requestFocus()
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(etDiary, InputMethodManager.SHOW_IMPLICIT)
                    },
                constImage.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        photoManager.showCameraDialog()
                    },
                etDiary.textChanges()
                    .doOnNext { diary ->
                        if (diary.length == 500) {
                            context?.let {
                                ToastUtil.showShortToast(it, getString(R.string.running_log_diary_max_length_alert))
                            }
                        }
                    }
                    .filter { diary -> diary.length <= 500 }
                    .subscribe { diary ->
                        viewModel.updateLogDiary(diary.toString())
                        tvRunningDiaryCharCount.text =
                            getString(R.string.running_log_diary_length, diary.length)
                        scrollView.post {
                            scrollView.smoothScrollTo(0, etDiary.bottom)
                        }
                    },
                constTeam.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        if (viewModel.logType.value == RunningLogType.ALONE) {
                            context?.let {
                                ToastUtil.showShortToast(it, "같이 뛰면 사용할 수 있어요!")
                            }
                        } else {
                            viewModel.gatheringId.value?.let {
                                navigate(
                                    RunningLogFragmentDirections.actionRunningLogFragmentToGroupProfilesFragment(
                                        it
                                    )
                                )
                            }
                        }
                    },
                switchVisibility.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        val prevVisibility = viewModel.logVisibility.value
                        viewModel.updateLogVisibility(!prevVisibility)
                    },
                btnSave.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        if (binding.etDiary.text.length >= 500) {
                            MessageDialog.createShow(
                                context = requireContext(),
                                message = getString(R.string.running_log_diary_max_length_alert),
                                buttonText = resources.getString(R.string.confirm)
                            )
                            return@subscribe
                        }

                        if (viewModel.logStamp.value == StampItem.unavailableStampItem) {
                            context?.let {
                                ToastUtil.showShortToast(it, getString(R.string.toast_stamp_missing))
                            }
                            return@subscribe
                        }

                        if (viewModel.logWeather.value == WeatherItem.defaultWeatherItem) {
                            context?.let {
                                ToastUtil.showShortToast(it, getString(R.string.toast_weather_missing))
                            }
                            return@subscribe
                        }

                        val userId = RunnerBeApplication.mTokenPreference.getUserId()
                        viewLifecycleOwner.lifecycleScope.launch {
                            val result = viewModel.postRunningLog(userId)

                            if (result.first) {
                                findNavController().popBackStack()
                            } else {
                                context?.let {
                                    ToastUtil.showShortToast(it, result.second ?: "Error")
                                }
                            }
                        }
                    },
            )
        }
    }

    private fun parseTemperature(temperature: String): String {
        return if (temperature.startsWith("-")) temperature
        else {
            "+${temperature}"
        }
    }
}