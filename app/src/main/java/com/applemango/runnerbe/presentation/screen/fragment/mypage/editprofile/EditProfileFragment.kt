package com.applemango.runnerbe.presentation.screen.fragment.mypage.editprofile

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentEditProfileBinding
import com.applemango.runnerbe.presentation.model.JobButtonId
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile), View.OnClickListener {

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
        binding.nameChangeBtn.setOnClickListener(this)
        binding.userNameEdit.addTextChangedListener {
            binding.nameFailTxt.isVisible =
                (it.toString() == viewModel.userInfo.value?.nickName) &&
                        viewModel.userInfo.value?.nameChanged != "Y"
        }
    }

    private fun observeBind() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (it.nameChanged == "Y") {
                viewModel.name.value = it.nickName?:""
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

    override fun onClick(v: View?) {
        when (v) {
            binding.nameChangeBtn -> {
                context?.let {
                    TwoButtonDialog.createShow(
                        context = it,
                        title = resources.getString(R.string.question_nickname_change),
                        firstButtonText = resources.getString(R.string.consider_more),
                        secondButtonText = resources.getString(R.string.yes),
                        firstEvent = {},
                        secondEvent = {
                            val name = binding.userNameEdit.text.toString()
                            if (name != viewModel.userInfo.value?.nickName) {
                                viewModel.nicknameChange(name)
                            }
                        }
                    )
                }

            }
        }
    }

}