package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.see

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentMyPostAttendanceSeeBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyPostAttendanceSeeFragment : BaseFragment<FragmentMyPostAttendanceSeeBinding>(R.layout.fragment_my_post_attendance_see) {
    private val viewModel: MyPostAttendanceSeeViewModel by viewModels()
    private val args : MyPostAttendanceSeeFragmentArgs by navArgs()

    @Inject
    lateinit var attendanceSeeAdapter: AttendanceSeeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getUserList(args.postId, args.userId)
        initUserListRecyclerView()
        setupUserListFlow()
    }

    private fun setupUserListFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userListFlow.collectLatest { userList ->
                    attendanceSeeAdapter.submitList(userList)
                }
            }
        }
    }

    private fun initUserListRecyclerView() {
        binding.rcvAttendanceSee.apply {
            adapter = attendanceSeeAdapter
            addItemDecoration(RecyclerViewItemDeco(context, 18))
        }
    }
}