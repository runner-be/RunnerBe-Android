package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.databinding.FragmentJoinedRunningBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.ToastUtil
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.BottomSpaceItemDecoration
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.checkedChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class JoinedRunningFragment : BaseFragment<FragmentJoinedRunningBinding>(R.layout.fragment_joined_running) {

    private val viewModel: JoinedRunningViewModel by viewModels()

    @Inject
    internal lateinit var joinedRunningPostAdapter: JoinedRunningPostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initListeners()
        initRunningRecyclerView()
        setupRunningLogsFlow()
        getUserPostings()
    }

    private fun getUserPostings() {
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        viewModel.getUserRunningPostings(userId)
    }

    private fun setupRunningLogsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userPostingFlow.collectLatest { postings ->
                    joinedRunningPostAdapter.submitList(postings)
                }
            }
        }
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.rgCategory.checkedChanges()
                .throttleFirst(500L, TimeUnit.MILLISECONDS)
                .subscribe { checkedId ->
                    when (checkedId) {
                        R.id.rb_all -> viewModel.updateSelectedCategory(JoinedRunningCategory.ALL)
                        R.id.rb_my -> viewModel.updateSelectedCategory(JoinedRunningCategory.MY)
                    }
                },
            binding.btnBack.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    goBack()
                }
        )
    }

    private fun initRunningRecyclerView() {
        binding.rcvPosting.apply {
            adapter = joinedRunningPostAdapter.apply {
                setPostFrom(PostCalledFrom.MY_PAGE)
                setPostClickListener(object: JoinedRunningClickListener {
                    override fun logWriteClick(post: Posting) {
                        try {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToRunningLogFragment(
                                    post.gatheringTime?.toLocalDate().toString(),
                                    null,
                                    null
                                )
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            ToastUtil.showShortToast(context, "러닝 로그가 존재하지 않습니다.")
                        }
                    }

                    override fun attendanceSeeClick(post: Posting) {
                        try {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToMyPostAttendanceSeeFragment(
                                    post.runnerList?.toTypedArray() ?: arrayOf()
                                )
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            ToastUtil.showShortToast(context, "참여한 러너가 없습니다.")
                        }
                    }

                    override fun attendanceManageClick(post: Posting) {
                        try {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToMyPostAttendanceAccessionFragment(
                                    post.runnerList?.toTypedArray() ?: arrayOf()
                                )
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            ToastUtil.showShortToast(context, "참여한 러너가 없습니다.")
                        }
                    }

                    override fun bookMarkClick(post: Posting) {

                    }

                    override fun postClick(post: Posting) {

                    }

                })
            }
            addItemDecoration(BottomSpaceItemDecoration(12.dpToPx(context)))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}