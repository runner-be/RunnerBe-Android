package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentRunningLogBinding
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.message.YesNoButtonDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.touches
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit

const val LOG_TYPE_ALONE = 0
const val GATHERING_ID_ALONE = 0

@AndroidEntryPoint
class RunningLogFragment : BaseFragment<FragmentRunningLogBinding>(R.layout.fragment_running_log) {

    private val viewModel: RunningLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("RunningLogFragment", "onCreate")
        arguments?.let {
            val strDate = it.getString("date")
            val logId = it.getInt("logId")
            val gatheringId = it.getInt("gatheringId")

            val logType = if (gatheringId == GATHERING_ID_ALONE) {
                RunningLogType.ALONE
            } else RunningLogType.TEAM

            viewModel.updateLogDate(strDate)
            viewModel.updateLogId(logId)
            viewModel.updateGatheringId(gatheringId)
            viewModel.updateLogType(logType)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initListeners()
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
                            Glide.with(requireContext())
                                .load(stamp.image)
                                .into(binding.ivStamp)
                            tvStamp.text = stamp.name
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