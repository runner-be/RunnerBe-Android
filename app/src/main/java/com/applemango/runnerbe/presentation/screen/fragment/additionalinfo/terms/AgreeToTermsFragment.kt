package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.terms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentAgreeToTermsBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import kotlinx.coroutines.launch

class AgreeToTermsFragment :
    BaseFragment<FragmentAgreeToTermsBinding>(R.layout.fragment_agree_to_terms) {

    private val viewModel: AgreeToTermsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allCheck.collect {
                binding.termsAllCheckBox.isChecked = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.collect {
                when (it) {
                    is AgreeToTermsAction.ActivityFinish -> { activity?.finish() }
                    is AgreeToTermsAction.MoveToWebView -> {
                        navigate(
                            AgreeToTermsFragmentDirections.actionAgreeToTermsFragmentToWebViewFragment(
                                title = it.title,
                                url = it.url
                            )
                        )
                    }
                    is AgreeToTermsAction.MoveToNext -> {
                        navigate(AgreeToTermsFragmentDirections.actionAgreeToTermsFragmentToYearInfoFragment())
                    }
                }
            }
        }
        binding.termsAllCheckBox.setOnClickListener {
            viewModel.serviceTerms.value = binding.termsAllCheckBox.isChecked
            viewModel.privacyTerms.value = binding.termsAllCheckBox.isChecked
            viewModel.locationServiceTerms.value = binding.termsAllCheckBox.isChecked
        }
    }
}