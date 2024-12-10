package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GroupProfilesFragment :
    BaseFragment<com.applemango.runnerbe.databinding.FragmentGroupProfilesBinding>(R.layout.fragment_group_profiles) {

    @Inject
    lateinit var profileAdapter: ProfileAdapter

    private val viewModel: GroupProfilesViewModel by viewModels()
    private val navArgs: GroupProfilesFragmentArgs by navArgs()

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.updateRunnerInfo(userId, navArgs.gatheringId)
        initGroupProfileRecyclerView()
        setupRunnerList()
        setupPostStampResult()
    }

    private fun setupRunnerList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.joinedRunnerListFlow.collectLatest { list ->
                    if (list.isNotEmpty()) {
                        binding.tvGroupProfileCount.text = getString(R.string.group_profile_count, list.size)
                        profileAdapter.submitList(list)
                    } else {
                        binding.tvGroupProfileCount.text = getString(R.string.group_profile_count, 0)
                    }
                }
            }
        }
    }

    private fun setupPostStampResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stampResult.collectLatest { response ->
                    when (response) {
                        is CommonResponse.Failed -> {
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun initGroupProfileRecyclerView() {
        with(binding.rcvProfile) {
            adapter = profileAdapter.apply {
                setOnProfileClickListener(object: OtherUserProfileClickListener {
                    override fun onProfileImageClicked(userId: Int) {
                        navigate(
                            GroupProfilesFragmentDirections.actionGroupProfilesFragmentToUserProfileFragment(userId)
                        )
                    }

                    override fun onProfileClicked(
                        position: Int,
                        targetUserId: Int,
                        stamp: StampItem?
                    ) {
                        viewModel.updateLastSelectedUserId(targetUserId)
                        StampBottomSheetDialog.createAndShow(
                            childFragmentManager,
                            null,
                            false
                        ) { stampItem ->
                            val userId = RunnerBeApplication.mTokenPreference.getUserId()
                            profileAdapter.updateProfileStampByPosition(position, stampItem.code)
                            viewModel.postStampToJoinedRunner(userId, navArgs.gatheringId, stampItem.code)
                        }
                    }

                    override fun onProfileLogClicked(userId: Int, logId: String?) {
                        try {
                            val parsedLogId = requireNotNull(logId).toInt()
                            navigate(
                                GroupProfilesFragmentDirections.actionGroupProfilesFragmentToRunningLogDetailFragment(
                                    userId, parsedLogId, 1
                                )
                            )
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }
                    }
                })
            }
            layoutManager = linearLayoutManager
        }
    }
}