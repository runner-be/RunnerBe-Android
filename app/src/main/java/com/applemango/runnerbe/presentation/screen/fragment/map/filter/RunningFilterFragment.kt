package com.applemango.runnerbe.presentation.screen.fragment.map.filter

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentRunningFilterBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.launch

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
                    when(it) {
                        is RunningFilterAction.MoveToBack -> {
                            setFragmentResult("filter", it.bundle)
                            navPopStack()
                        }
                    }
                }
            }
        }
    }

    private fun initSetting() {
        viewModel.setGenderTag(args.gender)
        viewModel.setJobTag(args.job)
        viewModel.isAllAgeChecked.value = isAllCheckAge(args.minAge, args.maxAge)
        if(!isAllCheckAge(args.minAge, args.maxAge)) {
            viewModel.recruitmentStartAge.value = args.minAge
            viewModel.recruitmentEndAge.value = args.maxAge
            binding.ageSlider.values = listOf(
                viewModel.recruitmentStartAge.value.toFloat(),
                viewModel.recruitmentEndAge.value.toFloat()
            )
        }
    }

    private fun isAllCheckAge(minAge : Int, maxAge : Int) = minAge == 0 && maxAge == 100
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
                    viewModel.backClicked()
                }
            })
    }
}