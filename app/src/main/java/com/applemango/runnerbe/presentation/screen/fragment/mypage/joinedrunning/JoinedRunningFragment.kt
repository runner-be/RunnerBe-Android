package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentJoinedRunningBinding
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.model.type.JoinedRunningCategory
import com.applemango.runnerbe.presentation.model.type.PostCalledFrom
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
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
    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    internal lateinit var joinedRunningPostAdapter: JoinedRunningPostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initListeners()
        initRunningRecyclerView()
        setupRunningLogsFlow()
        getUserPostings()
        binding.rgCategory.check(
            if (viewModel.selectedCategoryId.value == JoinedRunningCategory.ALL) R.id.rb_all
            else R.id.rb_my
        )
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
                    override fun logWriteClick(post: PostingModel) {
                        val userId = post.userId
                        val logId = post.logId

                        if (userId != null && logId != null) {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToRunningLogDetailFragment(
                                    userId,
                                    logId,
                                    0
                                )
                            )
                        } else {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToRunningLogFragment(
                                    post.gatheringTime?.toLocalDate().toString(),
                                    null,
                                    post.gatheringId.toString()
                                )
                            )
                        }
                    }

                    override fun attendanceSeeClick(post: PostingModel) {
                        try {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToMyPostAttendanceSeeFragment(
                                    post.postId,
                                    RunnerBeApplication.mTokenPreference.getUserId()
                                )
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            ToastUtil.showShortToast(context, "참여한 러너가 없습니다.")
                        }
                    }

                    override fun attendanceManageClick(post: PostingModel) {
                        try {
                            navigate(
                                JoinedRunningFragmentDirections.actionJoinedRunningFragmentToMyPostAttendanceAccessionFragment(
                                    post.postId,
                                    RunnerBeApplication.mTokenPreference.getUserId()
                                )
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                            ToastUtil.showShortToast(context, "참여한 러너가 없습니다.")
                        }
                    }

                    override fun bookMarkClick(post: PostingModel) {
                        mainViewModel.bookmarkStatusChange(post)
                        viewModel.updatePostBookmark(post)
                    }

                    override fun postClick(post: PostingModel) {
                        navigate(
                            JoinedRunningFragmentDirections.actionJoinedRunningFragmentToPostDetailFragment(
                                post
                            )
                        )
                    }

                })
            }
            addItemDecoration(BottomSpaceItemDecoration(12.dpToPx(context)))
            itemAnimator = null
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}