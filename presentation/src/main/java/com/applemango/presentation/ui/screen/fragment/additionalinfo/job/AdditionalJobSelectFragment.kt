package com.applemango.presentation.ui.screen.fragment.additionalinfo.job

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.presentation.ui.model.type.JobButtonId
import com.applemango.presentation.ui.screen.dialog.message.MessageDialog
import com.applemango.presentation.ui.screen.fragment.additionalinfo.AdditionalInfoAction
import com.applemango.presentation.ui.screen.fragment.additionalinfo.AdditionalInfoViewModel
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.state.UiState
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentAdditionalJobSelectBinding
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
                        else -> {
                            Log.e("AdditionalJobSelectFragment", "onViewCreated - when - else")
                        }
                    }
                }
            }

            launch {
                viewModel.actions.collect {
                    when(it) {
                        is AdditionalInfoAction.MoveToBack -> { navPopStack() }
                        is AdditionalInfoAction.ActivityFinish -> { activity?.finish() }
                        else -> { Log.e(this.javaClass.name, "onViewCreated - when - else - AdditionalInfoAction") }
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