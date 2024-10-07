package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.FragmentRunningLogDetailBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.dialog.weather.getWeatherItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.postdetail.PostDetailFragmentArgs
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
        viewModel.updateRunningLogArgs(Pair(args.userId, args.logId))
    }

    private fun initMemberStampRecyclerView() {
        binding.rcvTeamStamp.apply {
            adapter = gotStampAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

                    gotStampAdapter.submitList(memberStamps)
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
                        ivTeam.setImageResource(R.drawable.ic_team_default)
                    }
                    tvUserStamp.text = getString(R.string.running_log_got_stamp, runningLogDetail.nickname)
                    tvTeamDetail.text = "${it.gatheringCount} 명"
                    switchVisibility.apply {
                        isChecked = it.runningLog.isOpened == 1
                        isEnabled = false
                    }
                }
            }
        }
    }
}