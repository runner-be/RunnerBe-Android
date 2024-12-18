package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

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
import com.applemango.runnerbe.databinding.FragmentRunningLogDetailBinding
import com.applemango.runnerbe.presentation.screen.dialog.menu.MenuDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.ToastUtil
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.RightSpaceItemDecoration
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class RunningLogDetailFragment :
    BaseFragment<FragmentRunningLogDetailBinding>(R.layout.fragment_running_log_detail) {
    private val viewModel: RunningLogDetailViewModel by viewModels()
    private val args: RunningLogDetailFragmentArgs by navArgs()

    private val logId: Int by lazy {
        args.logId
    }
    private val isOtherUser: Boolean by lazy {
        args.isOtherUser == 1
    }

    @Inject
    lateinit var gotStampAdapter: GotStampAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initMemberStampRecyclerView()
        initIsOtherUserLog(this.isOtherUser)
        initListeners()
        if (!isOtherUser) {
            initMenuClickListeners()
        }
        setupDeleteRunningLogResult()
        setupRunningLogDetail()
        viewModel.getLogDetail(args.userId, logId)
    }

    private fun initMemberStampRecyclerView() {
        binding.rcvTeamStamp.apply {
            adapter = gotStampAdapter.apply {
                initGotStampClickListener {
                    navigate(
                        RunningLogDetailFragmentDirections.actionRunningLogDetailFragmentToOtherUserProfileFragment(
                            args.userId
                        )
                    )
                }
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(RightSpaceItemDecoration(10.dpToPx(requireContext())))
        }
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.btnBack.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    goBack()
                },
            binding.constTeam.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    val gatheringId = viewModel.runningLogDetail.value?.runningLog?.gatheringId

                    if (gatheringId == null) {
                        context?.let {
                            ToastUtil.showShortToast(it, "같이 뛰면 사용할 수 있어요!")
                        }
                    } else {
                        navigate(
                            RunningLogDetailFragmentDirections.actionRunningLogDetailFragmentToGroupProfilesFragment(
                                gatheringId
                            )
                        )
                    }
                },
            binding.ivPhoto.clicks()
                .subscribe {
                    val imageUrl = viewModel.runningLogDetail.value?.runningLog?.imageUrl

                    if (imageUrl != null) {
                        navigate(
                            RunningLogDetailFragmentDirections.actionRunningLogDetailFragmentToImageDetailFragment(
                                "",
                                arrayOf(imageUrl),
                                1
                            )
                        )
                    }
                }
        )
    }

    private fun initIsOtherUserLog(isOtherUser: Boolean) {
        val visibility = if (isOtherUser) View.GONE else View.VISIBLE
        binding.ivMenu.visibility = visibility
        binding.constVisibility.visibility = visibility
    }

    private fun initMenuClickListeners() {
        binding.ivMenu.setOnClickListener {
            context?.let {
                val runningLog = viewModel.runningLogDetail.value?.runningLog!!
                val stringDate = runningLog.runnedDate.toLocalDate().toString()
                val userId = RunnerBeApplication.mTokenPreference.getUserId()
                val gatheringId = runningLog.gatheringId
                MenuDialog.createAndShow(
                    it,
                    userId,
                    logId,
                    { lId1 ->
                        navigate(
                            RunningLogDetailFragmentDirections.actionRunningLogDetailFragmentToRunningLogFragment(
                                stringDate, lId1.toString(), gatheringId.toString()
                            )
                        )
                    },
                    { uId2, lId2 ->
                        viewModel.deleteRunningLog(uId2, lId2)
                        goBack()
                    }
                )
            }
        }
    }

    private fun setupDeleteRunningLogResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteRunningLogResultFlow.collect { response ->
                when (response) {
                    is CommonResponse.Success<*> -> goBack()
                    is CommonResponse.Failed -> Toast.makeText(
                        context,
                        getString(R.string.error_failed),
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> Toast.makeText(
                        context,
                        getString(R.string.error_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupRunningLogDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.runningLogDetail.collect {
                    val gotStampList = it?.gotStamp
                    gotStampList?.let {
                        with(binding) {
                            if (it.isEmpty()) {
                                tvTeamStampEmpty.visibility = View.VISIBLE
                            } else {
                                tvTeamStampEmpty.visibility = View.GONE
                                gotStampAdapter.submitList(it)
                            }
                        }
                    }
                }
            }
        }
    }
}