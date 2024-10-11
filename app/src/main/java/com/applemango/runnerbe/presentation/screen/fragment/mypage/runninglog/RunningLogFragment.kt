package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.applemango.runnerbe.presentation.screen.dialog.weather.getWeatherItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
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
    private val navArgs : RunningLogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val strDate = navArgs.date
        val logId = navArgs.logId
        val gatheringId = navArgs.gatheringId

        Log.e("RunningLogFragment onCreate gatheringId", gatheringId.toString())
        val logType = if (gatheringId != null) {
            RunningLogType.TEAM
        } else RunningLogType.ALONE
        Log.e("RunningLogFragment onCreate logType", logType.toString())

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
                        viewModel.updateLogDate(parseLocalDateToKorean(log.runnedDate.toLocalDate()))
                        viewModel.updateStamp(getStampItemByCode(log.stampCode))
                        viewModel.updateLogDiary(log.contents)
                        viewModel.updateDegreeAndWeather(log.weatherDegree.toString(), getWeatherItemByCode(log.weatherCode))
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
                            prevStamp
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
                            tvDegree.text = parseTemperature(temperature)
                            viewModel.updateDegreeAndWeather(temperature, weather)
                        }
                    },
                constDiary.touches()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        etDiary.requestFocus()
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(etDiary, InputMethodManager.SHOW_IMPLICIT)
                    },
                constImage.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        Toast.makeText(context, "이미지 추가", Toast.LENGTH_SHORT).show()
                    },
                etDiary.textChanges()
                    .subscribe {
                        viewModel.updateLogDiary(it.toString())
                        tvRunningDiaryCharCount.text = getString(R.string.running_log_diary_length, it.length)
                    },
                constTeam.clicks()
                    .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                    .subscribe {
                        if (viewModel.logType.value != RunningLogType.ALONE) {
                            val logId = 123
                            navigate(RunningLogFragmentDirections.actionRunningLogFragmentToGroupProfilesFragment(logId))
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

                        val userId = RunnerBeApplication.mTokenPreference.getUserId()
                        viewLifecycleOwner.lifecycleScope.launch {
                            val result = viewModel.postRunningLog(userId)

                            if (result.first) {
                                findNavController().popBackStack()
                            } else {
                                Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
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