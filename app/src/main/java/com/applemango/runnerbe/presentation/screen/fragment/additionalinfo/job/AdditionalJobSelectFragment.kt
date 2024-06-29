package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.job

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentAdditionalJobSelectBinding
import com.applemango.runnerbe.presentation.model.JobButtonId
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.AdditionalInfoAction
import com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.AdditionalInfoViewModel
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdditionalJobSelectFragment: BaseFragment<FragmentAdditionalJobSelectBinding>(R.layout.fragment_additional_job_select) {

    private val viewModel : AdditionalInfoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.registerState.collect {
                    context?.let { context ->
                        if (it is UiState.Loading) showLoadingDialog(context)
                        else dismissLoadingDialog()
                    }
                    when(it) {
                        is UiState.Success -> moveToNext()
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
                viewModel.actions.collect {
                    when(it) {
                        is AdditionalInfoAction.MoveToBack -> { navPopStack() }
                        is AdditionalInfoAction.ActivityFinish -> { activity?.finish() }
                    }
                }
            }

            launch {
                viewModel.jobRadioChecked.collect {
                    binding.registerButton.isEnabled = JobButtonId.findById(it) != null
                }
            }
        }
    }

    private fun moveToNext() {
        navigate(AdditionalJobSelectFragmentDirections.actionAdditionalJobSelectFragmentToRegisterCompleteFragment())
    }
}