package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.accession

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentMyPostAttendanceAccessionBinding
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPostAttendanceAccessionFragment : BaseFragment<FragmentMyPostAttendanceAccessionBinding>(R.layout.fragment_my_post_attendance_accession) {

    private val viewModel : MyPostAttendanceAccessionViewModel by viewModels()
    private val args : MyPostAttendanceAccessionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.postId = args.postId
        // TODO - 네비게이션 아규먼트 수정 후 usecase 호출해서 리스트 보여주기...
//        viewModel.userListUpdate(args.users.toList())
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
                        //오프라인 발생 어쩌구 다이얼로그
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