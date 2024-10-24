package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentOtherUserProfileBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.WeeklyCalendarAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.initWeekDays
import com.applemango.runnerbe.util.ToastUtil
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.RightSpaceItemDecoration
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OtherUserProfileFragment : BaseFragment<FragmentOtherUserProfileBinding>(R.layout.fragment_other_user_profile) {
    private val viewModel: OtherUserProfileViewModel by viewModels()

    private val navArgs: OtherUserProfileFragmentArgs by navArgs()
    @Inject
    lateinit var otherUserJoinedPostAdapter: OtherUserJoinedPostAdapter
    @Inject
    lateinit var weeklyCalendarAdapter: WeeklyCalendarAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.updateTargetUserId(navArgs.targetUserId)
        initJoinedRunningPostRecyclerView()
        initWeeklyCalendarAdapter()
        setupUserData()
        initDisposables()
    }

    private fun setupUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setupOtherUser()
                setupWeeklyCalendar()
                setupJoinedRunningPost()
            }
        }
    }

    private fun CoroutineScope.setupOtherUser() = launch {
        viewModel.otherUserProfileFlow.collect { _ ->

        }
    }

    private fun CoroutineScope.setupWeeklyCalendar() = launch {
        viewModel.userRunningLogs.collect { list ->
            weeklyCalendarAdapter.submitList(initWeekDays(LocalDate.now(), list))
        }
    }

    private fun CoroutineScope.setupJoinedRunningPost() = launch {
        viewModel.userPostings.collect { list ->
            otherUserJoinedPostAdapter.submitList(list)
        }
    }

    private fun initDisposables() {
        compositeDisposable.addAll(
            getCalendarClickDisposable()
        )
    }

    private fun getCalendarClickDisposable() = binding.ivCalendar.clicks()
        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
        .subscribe {
            viewModel.targetUserIdFlow.value?.let { userId ->
                navigate(
                    OtherUserProfileFragmentDirections.actionUserProfileFragmentToMonthlyCalendarFragment(
                        userId,
                        1
                    )
                )
            }
        }

    private fun initWeeklyCalendarAdapter() {
        binding.rcvCalendarWeekly.apply {
            adapter = weeklyCalendarAdapter
            weeklyCalendarAdapter.setIsOtherUser(true)
            weeklyCalendarAdapter.setOnDateClickListener { item ->
                if (item.runningLog == null) return@setOnDateClickListener

                try {
                    val targetUserId = requireNotNull(viewModel.userInfo.value?.userId)
                    val logId = item.runningLog.logId

                    navigate(
                        OtherUserProfileFragmentDirections.actionUserProfileFragmentToRunningLogDetailFragment(
                            targetUserId,
                            logId
                        )
                    )
                } catch (e: IllegalArgumentException) {
                    ToastUtil.showShortToast(context, getString(R.string.error_failed))
                    e.printStackTrace()
                }
            }
            layoutManager = GridLayoutManager(context, 7)
        }
    }

    private fun initJoinedRunningPostRecyclerView() {
        binding.rcvJoinedRunningPost.apply {
            adapter = otherUserJoinedPostAdapter
            otherUserJoinedPostAdapter.setOnPostClickListener { item ->
                // TODO
//                navigate(
//                    OtherUserProfileFragmentDirections.actionUserProfileFragmentToPostDetailFragment(
//                        item
//                    )
//                )
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RightSpaceItemDecoration(12.dpToPx(context)))
        }
    }
}