package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentRunningLogDetailBinding
import com.applemango.runnerbe.presentation.screen.dialog.menu.MenuDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.dialog.weather.getWeatherItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.parseLocalDateToKorean
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunningLogDetailFragment : BaseFragment<FragmentRunningLogDetailBinding>(R.layout.fragment_running_log_detail) {
    private val viewModel: RunningLogDetailViewModel by viewModels()
    private val args: RunningLogDetailFragmentArgs by navArgs()

    @Inject
    lateinit var gotStampAdapter: GotStampAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMemberStampRecyclerView()
        setupRunningLogDetail()
        initMenuClickListeners()
        initClickListeners()
        setupDeleteRunningLogResult()
        viewModel.updateRunningLogArgs(Pair(args.userId, args.logId))
    }

    private fun initMemberStampRecyclerView() {
        binding.rcvTeamStamp.apply {
            adapter = gotStampAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initClickListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                goBack()
            }
        }
    }

    private fun initMenuClickListeners() {
        binding.ivMenu.setOnClickListener {
            context?.let {
                val stringDate = viewModel.runningLogDate.value
                val userId = RunnerBeApplication.mTokenPreference.getUserId()
                val logId = viewModel.runningLogArgsFlow.value.second
                val gatheringId = viewModel.runningLogGatheringId.value
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
                when(response) {
                    is CommonResponse.Success<*> -> goBack()
                    is CommonResponse.Failed -> Toast.makeText(context, getString(R.string.error_failed), Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(context, getString(R.string.error_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRunningLogDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.runningLogDetailFlow.collect {
                with(binding) {
                    val runningLogDetail = it.runningLog
                    val stampItem = getStampItemByCode(runningLogDetail.stampCode)
                    val weatherItem = getWeatherItemByCode(runningLogDetail.weatherCode)
                    val memberStamps = it.gotStamp

                    if (memberStamps.isEmpty()) {
                        tvTeamStampEmpty.visibility = View.VISIBLE
                    } else {
                        tvTeamStampEmpty.visibility = View.GONE
                        gotStampAdapter.submitList(memberStamps)
                    }
                    viewModel.updateRunningLogDate(runningLogDetail.runnedDate.toLocalDate().toString())
                    tvDateTime.text = parseLocalDateToKorean(runningLogDetail.runnedDate.toLocalDate())
                    Glide.with(binding.root.context)
                        .load(stampItem.image)
                        .into(binding.ivStamp)
                    tvStamp.text = stampItem.description
                    tvDiary.text = runningLogDetail.contents
                    runningLogDetail.imageUrl?.let {
                        Glide.with(binding.root.context)
                            .load(runningLogDetail.imageUrl)
                            .into(binding.ivPhoto)
                    } ?: run {
                        binding.ivPhoto.visibility = View.GONE
                    }
                    tvDegree.text = runningLogDetail.weatherDegree.toString()
                    Glide.with(binding.root.context)
                        .load(weatherItem.image)
                        .into(binding.ivWeather)
                    if (runningLogDetail.gatheringId == null) {
                        ivTeam.setImageResource(R.drawable.ic_team_lock)
                    } else {
                        viewModel.updateRunningLogGatheringId(runningLogDetail.gatheringId)
                        ivTeam.setImageResource(R.drawable.ic_team_default)
                    }
                    tvUserStamp.text = getString(R.string.running_log_got_stamp, runningLogDetail.nickname)
                    tvTeamDetail.text = getString(R.string.group_profile_count_2, it.gatheringCount)
                    switchVisibility.apply {
                        isChecked = it.runningLog.isOpened == 1
                        isEnabled = false
                    }
                }
            }
        }
    }
}