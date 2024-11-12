package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.databinding.FragmentMyPostAttendanceAccessionBinding
import com.applemango.runnerbe.presentation.model.listener.AttendanceAccessionClickListener
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyPostAttendanceAccessionFragment : BaseFragment<FragmentMyPostAttendanceAccessionBinding>(R.layout.fragment_my_post_attendance_accession) {

    private val viewModel : MyPostAttendanceAccessionViewModel by viewModels()
    private val args : MyPostAttendanceAccessionFragmentArgs by navArgs()

    @Inject
    lateinit var attendanceAccessionAdapter: AttendanceAccessionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getUserList(args.postId, args.userId)
        initAttendanceAccessionAdapter()
        setupSubmitState()
        setupUserListFlow()
    }

    private fun initAttendanceAccessionAdapter() {
        binding.rcvAttendanceAccession.apply {
            adapter = attendanceAccessionAdapter.apply {
                setAccessionClickListener(object: AttendanceAccessionClickListener {
                    override fun onAcceptClick(userInfo: UserInfo) {
                        viewModel.select()
                        userInfo.attendance = 1
                    }

                    override fun onRefuseClick(userInfo: UserInfo) {
                        viewModel.select()
                        userInfo.attendance = 0
                    }
                })
            }
        }
    }

    private fun setupUserListFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userListFlow.collectLatest { userList ->
                    attendanceAccessionAdapter.submitList(userList)
                }
            }
        }
    }

    private fun setupSubmitState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.submitState.collect {
                context?.let { context ->
                    if (it is UiState.Loading) showLoadingDialog(context)
                    else dismissLoadingDialog()
                }
                when(it) {
                    is UiState.Success -> navPopStack()
                    is UiState.Failed -> {
                        context?.let { context ->
                            MessageDialog.createShow(
                                context = context,
                                message = it.message,
                                buttonText = resources.getString(R.string.confirm)
                            )
                        }
                    }
                    is UiState.NetworkError -> {
                        Log.e("error", it.toString())
                    }
                    else -> {
                        Log.e("MyPostAttendanceAccessionFragment", "onViewCreated - when - else")
                    }
                }
            }
        }
    }
}