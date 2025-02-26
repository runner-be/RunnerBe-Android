package com.applemango.presentation.ui.screen.fragment.mypage.mypost.see

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.applemango.presentation.ui.screen.deco.RecyclerViewItemDeco
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentMyPostAttendanceSeeBinding
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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
        viewModel.getUserList(args.postId)
        initUserListRecyclerView()
        setupUserListFlow()
        setupListeners()
    }

    private fun setupListeners() {
        compositeDisposable.addAll(
            binding.backBtn.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    goBack()
                }
        )
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
            adapter = attendanceSeeAdapter.apply {
                initProfileClickListener { userId ->
                    navigate(
                        MyPostAttendanceSeeFragmentDirections.actionMyPostAttendanceSeeFragmentToOtherUserProfileFragment(
                            userId
                        )
                    )
                }
            }
            addItemDecoration(RecyclerViewItemDeco(context, 18))
        }
    }
}