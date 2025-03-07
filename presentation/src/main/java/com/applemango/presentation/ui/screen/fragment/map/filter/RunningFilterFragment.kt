package com.applemango.presentation.ui.screen.fragment.map.filter

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.util.ToastUtil
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentRunningFilterBinding
import com.google.android.material.slider.RangeSlider
import com.jakewharton.rxbinding4.view.clicks
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class RunningFilterFragment :
    BaseFragment<FragmentRunningFilterBinding>(R.layout.fragment_running_filter) {

    private val viewModel: RunningFilterViewModel by viewModels()
    private val args: RunningFilterFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initSetting()
        filterSetting()
        sliderSetting()
        initListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.recruitmentStartAge.collect {
                    binding.ageSlider.values = listOf(
                        viewModel.recruitmentStartAge.value.toFloat(),
                        viewModel.recruitmentEndAge.value.toFloat()
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.recruitmentEndAge.collect {
                    binding.ageSlider.values = listOf(
                        viewModel.recruitmentStartAge.value.toFloat(),
                        viewModel.recruitmentEndAge.value.toFloat()
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.actions.collect {
                    when (it) {
                        is RunningFilterAction.MoveToBack -> {
                            if (viewModel.paceCheckedList.value.isEmpty()) {
                                context?.let { ctxt ->
                                    ToastUtil.showShortToast(ctxt, getString(R.string.toast_error_pace_selected_empty))
                                }
                            } else {
                                setFragmentResult("filter", it.bundle)
                                navPopStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getIsTooltipVisible(): Boolean =
        binding.tvTooltipPaceInfo.visibility == View.VISIBLE

    private fun controlPaceTooltip(isTooltipVisible: Boolean) {
        if (isTooltipVisible) {
            hidePaceTooltip()
        } else {
            showPaceTooltipWithTimeout()
        }
    }

    private fun showPaceTooltipWithTimeout() {
        with(binding) {
            tvTooltipPaceInfo.visibility = View.VISIBLE
            ivTooltipDirection.visibility = View.VISIBLE

            tvTooltipPaceInfo.postDelayed({
                tvTooltipPaceInfo.visibility = View.GONE
                ivTooltipDirection.visibility = View.GONE
            }, 2500L)
        }
    }

    private fun hidePaceTooltip() {
        with(binding) {
            tvTooltipPaceInfo.visibility = View.GONE
            ivTooltipDirection.visibility = View.GONE
        }
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.ivPaceInfo.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    val isTooltipVisible = getIsTooltipVisible()
                    controlPaceTooltip(isTooltipVisible)
                },
        )
    }

    private fun initSetting() {
        viewModel.setPaceTags(args.paces.toList())
        viewModel.setGenderTag(args.gender)
        viewModel.setAfterPartyTag(args.afterParty)
        viewModel.setJobTag(args.job)
        viewModel.isAllAgeChecked.value = isAllCheckAge(args.minAge, args.maxAge)
        if (!isAllCheckAge(args.minAge, args.maxAge)) {
            viewModel.recruitmentStartAge.value = args.minAge
            viewModel.recruitmentEndAge.value = args.maxAge
            binding.ageSlider.values = listOf(
                viewModel.recruitmentStartAge.value.toFloat(),
                viewModel.recruitmentEndAge.value.toFloat()
            )
        }
    }

    private fun isAllCheckAge(minAge: Int, maxAge: Int) = minAge == 0 && maxAge == 100
    private fun sliderSetting() {
        binding.ageSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            val ages = slider.values
            viewModel.recruitmentStartAge.value = ages[0].toInt()
            viewModel.recruitmentEndAge.value = ages[1].toInt()
        })
    }

    private fun filterSetting() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val count = viewModel.paceList.value.count { it.isSelected }
                    if (count != 0) {
                        viewModel.backClicked()
                    } else {
                        context?.let { ctxt ->
                            ToastUtil.showShortToast(ctxt, getString(R.string.toast_error_pace_selected_empty))
                        }
                    }
                }
            })
    }
}