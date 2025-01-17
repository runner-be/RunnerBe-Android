package com.applemango.runnerbe.presentation.screen.fragment.mypage.editprofile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentEditProfileBinding
import com.applemango.runnerbe.presentation.model.type.JobButtonId
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private var currentRadioButton: Int? = null
    private val viewModel: EditProfileViewModel by viewModels()
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.fragment = this
        binding.buttonClick = jobButtonClick()
        viewModel.userInfo.value = args.userData
        observeBind()
        initListeners()
    }

    private fun observeBind() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (it.nameChanged == "Y") {
                viewModel.name.value = it.nickname?:""
            }
            initJob()
        }
        viewModel.jobChangeState.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it is UiState.Loading) showLoadingDialog(context)
                else dismissLoadingDialog()
            }
            when (it) {
                is UiState.NetworkError -> {
                    //오프라인 발생 어쩌구 다이얼로그
                }
                is UiState.Failed -> {
                    context?.let { context ->
                        MessageDialog.createShow(
                            context = context,
                            message = it.message,
                            buttonText = resources.getString(R.string.confirm)
                        )
                    }
                }
                is UiState.Success -> {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.complete_change_job),
                        Toast.LENGTH_SHORT
                    ).show()
                    refreshBack()
                }

                else -> {
                    Log.e("EditProfileFragment", "observeBind - when - else")
                }
            }
        }
        viewModel.nicknameChangeState.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it is UiState.Loading) showLoadingDialog(context)
                else dismissLoadingDialog()
            }
            when (it) {
                is UiState.NetworkError -> {
                    //오프라인 발생 어쩌구 다이얼로그
                }
                is UiState.Failed -> {
                    context?.let { context ->
                        MessageDialog.createShow(
                            context = context,
                            message = it.message,
                            buttonText = resources.getString(R.string.confirm)
                        )
                    }
                }
                is UiState.Success -> {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.complete_change_nickname),
                        Toast.LENGTH_SHORT
                    ).show()
                    refreshBack()
                }

                else -> {
                    Log.e("EditProfileFragment", "observeBind - when - else")
                }
            }
        }
    }

    private fun initJob() {
        val currentJob = JobButtonId.findByName(viewModel.userInfo.value?.job ?: "")
        viewModel.radioChecked.value = currentJob?.id
        viewModel.currentJob = currentJob?.job ?: ""
        currentRadioButton = currentJob?.id
    }

    private fun refreshBack() {
        requireActivity().supportFragmentManager.setFragmentResult("refresh", bundleOf())
        navPopStack()
    }

    private fun jobButtonClick() = OnCheckedChangeListener { btn, isCheck ->
        if (viewModel.userInfo.value?.job != JobButtonId.findById(btn.id)?.koreanName && isCheck) {
            context?.let {
                TwoButtonDialog.createShow(
                    context = it,
                    title = resources.getString(R.string.question_job_change),
                    firstButtonText = resources.getString(R.string.no),
                    secondButtonText = resources.getString(R.string.yes),
                    firstEvent = {
                        initJob()
                    },
                    secondEvent = {
                        viewModel.currentJob = JobButtonId.findById(btn.id)?.job ?: ""
                        viewModel.jobChange(viewModel.currentJob)
                    }
                )
            }
        }
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.tieNickname.textChanges()
                .filter { it.length <= 8 }
                .subscribe {
                    binding.nameFailTxt.isVisible =
                        (it.toString() == viewModel.userInfo.value?.nickname) &&
                                viewModel.userInfo.value?.nameChanged != "Y"
                },
            binding.nameChangeBtn.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    context?.let {
                        TwoButtonDialog.createShow(
                            context = it,
                            title = resources.getString(R.string.question_nickname_change),
                            firstButtonText = resources.getString(R.string.consider_more),
                            secondButtonText = resources.getString(R.string.yes),
                            firstEvent = {},
                            secondEvent = {
                                val name = binding.tieNickname.text.toString()
                                if (name != viewModel.userInfo.value?.nickname) {
                                    viewModel.nicknameChange(name)
                                }
                            }
                        )
                    }
                }
        )
    }
}