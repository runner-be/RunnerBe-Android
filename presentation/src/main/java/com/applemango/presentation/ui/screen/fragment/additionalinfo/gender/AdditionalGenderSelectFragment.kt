package com.applemango.presentation.ui.screen.fragment.additionalinfo.gender

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.presentation.ui.screen.fragment.additionalinfo.AdditionalInfoAction
import com.applemango.presentation.ui.screen.fragment.additionalinfo.AdditionalInfoViewModel
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentAdditionalGenderSelectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdditionalGenderSelectFragment : BaseFragment<FragmentAdditionalGenderSelectBinding>(R.layout.fragment_additional_gender_select){

    private val viewModel : AdditionalInfoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.genderRadioChecked.collect {
                    binding.nextButton.isEnabled = (it == binding.femaleButton.id || it == binding.maleButton.id)
                }
            }
            launch {
                viewModel.actions.collect {
                    when(it) {
                        is AdditionalInfoAction.MoveToNext -> {
                            navigate(AdditionalGenderSelectFragmentDirections.actionAdditionalGenderSelectFragmentToAdditionalJobSelectFragment())
                        }
                        is AdditionalInfoAction.MoveToBack -> { navPopStack() }
                        is AdditionalInfoAction.ActivityFinish -> { activity?.finish() }
                    }
                }
            }
        }
    }
}