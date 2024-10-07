package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentGroupProfilesBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GroupProfilesFragment :
    BaseFragment<FragmentGroupProfilesBinding>(R.layout.fragment_group_profiles) {

    @Inject
    lateinit var profileAdapter: ProfileAdapter

    private val viewModel: GroupProfilesViewModel by viewModels()
    private val navArgs: GroupProfilesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        viewModel.updateRunnerInfo(userId, navArgs.gatheringId)
        initGroupProfileRecyclerView()
        initClickListeners()
        setupRunnerList()
    }

    private fun setupRunnerList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.runnerListFlow.collectLatest { list ->
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

    private fun initClickListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                goBack()
            }
        }
    }

    private fun initGroupProfileRecyclerView() {
        with(binding.rcvProfile) {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = profileAdapter.apply {
                setOnProfileClickListener { position, stamp ->
                    StampBottomSheetDialog.createAndShow(
                        childFragmentManager,
                        stamp ?: StampItem(
                            "default",
                            R.drawable.ic_stamp_1_personal,
                            context.getString(R.string.stamp_1_name),
                            context.getString(R.string.stamp_1_description),
                            true
                        )
                    ) { stampItem ->
                        profileAdapter.updateProfileStampByPosition(position, stampItem)
                    }
                }
            }
            layoutManager = linearLayoutManager
        }
    }
}