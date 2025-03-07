package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.weekly.WeeklyCalendarPagerAdapter
import com.applemango.presentation.util.dpToPx
import com.applemango.presentation.util.recyclerview.RightSpaceItemDecoration
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentOtherUserProfileBinding
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OtherUserProfileFragment :
    BaseFragment<FragmentOtherUserProfileBinding>(R.layout.fragment_other_user_profile) {
    private val viewModel: OtherUserProfileViewModel by viewModels()

    private val navArgs: OtherUserProfileFragmentArgs by navArgs()

    @Inject
    lateinit var otherUserJoinedPostAdapter: OtherUserJoinedPostAdapter

    private var _weeklyCalendarPagerAdapter: WeeklyCalendarPagerAdapter? = null
    private val weeklyCalendarPagerAdapter get() = _weeklyCalendarPagerAdapter!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getOtherUserProfile(navArgs.targetUserId)
        initJoinedRunningPostRecyclerView()
        initDisposables()
        initWeeklyViewPagerAdapter()
        setupWeeklyViewPagerPosition()
        setupJoinedPostings()
        setupWeeklyRunningCount()
    }

    private fun initWeeklyViewPagerAdapter() {
        binding.vpWeeklyCalendar.apply {
            _weeklyCalendarPagerAdapter = WeeklyCalendarPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
                true,
                navArgs.targetUserId
            ) { groupCount, personalCount ->
                viewModel.addViewPagerCounts(groupCount, personalCount)
                binding.tvStampWeekly.text = if (groupCount == 0 && personalCount == 0) {
                    getString(R.string.calendar_monthly_statistic_empty)
                } else {
                    getString(
                        R.string.calendar_monthly_statistic,
                        groupCount, personalCount
                    )
                }
            }
            adapter = weeklyCalendarPagerAdapter
            setCurrentItem(viewModel.currentWeeklyViewPagerPosition.value, false)
        }
        initDotsIndicator()
    }

    private fun setupWeeklyRunningCount() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewpagerRunningCount.collectLatest { (groupCount, personalCount) ->
                    binding.tvStampWeekly.text = if (groupCount == 0 && personalCount == 0) {
                        getString(R.string.calendar_monthly_statistic_empty)
                    } else {
                        getString(R.string.calendar_monthly_statistic,
                            groupCount,
                            personalCount)
                    }
                }
            }
        }
    }

    private fun setupJoinedPostings() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userJoinedPosting.collectLatest {
                    if (it.isNotEmpty()) {
                        otherUserJoinedPostAdapter.submitList(it)
                        binding.tvJoinedRunningEmpty.visibility = View.GONE
                    } else {
                        binding.tvJoinedRunningEmpty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupWeeklyViewPagerPosition() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeeklyViewPagerPosition.collectLatest { position ->
                    binding.vpWeeklyCalendar.setCurrentItem(position, false)
                }
            }
        }
    }


    private fun initDotsIndicator() {
        binding.indicatorDots.attachTo(binding.vpWeeklyCalendar)
    }

    private fun initDisposables() {
        compositeDisposable.addAll(
            getBackBtnDisposable(),
            getCalendarClickDisposable(),
            getJoinedRunningClickDisposable()
        )
    }

    private fun getBackBtnDisposable() = binding.btnBack.clicks()
        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
        .subscribe {
            goBack()
        }

    private fun getJoinedRunningClickDisposable() = binding.constJoinedRunningPostTitle.clicks()
        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
        .subscribe {
            val user = viewModel.otherUserInfo.value
            user?.let {
                navigate(
                    OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToOtherUserJoinedRunningFragment(
                        it.userId,
                        it.nickName
                    )
                )
            }
        }

    private fun getCalendarClickDisposable() = binding.ivCalendar.clicks()
        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
        .subscribe {
            val user = viewModel.otherUserInfo.value
            user?.let {
                navigate(
                    OtherUserProfileFragmentDirections.actionUserProfileFragmentToMonthlyCalendarFragment(
                        it.userId,
                        1
                    )
                )
            }
        }

    private fun initJoinedRunningPostRecyclerView() {
        binding.rcvJoinedRunningPost.apply {
            adapter = otherUserJoinedPostAdapter
            otherUserJoinedPostAdapter.setOnPostClickListener { item ->
                navigate(
                    OtherUserProfileFragmentDirections.actionUserProfileFragmentToPostDetailFragment(
                        item
                    )
                )
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RightSpaceItemDecoration(12.dpToPx(context)))
        }
    }
}