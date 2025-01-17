package com.applemango.runnerbe.presentation.screen.fragment.main.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentAlarmBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {
    private val viewModel: AlarmViewModel by viewModels()

    @Inject
    lateinit var alarmAdapter: AlarmAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initNotificationAdapter()
        setupNotifications()
        viewModel.getNotifications()
    }

    private fun initNotificationAdapter() {
        with(binding.rcvNotification) {
            adapter = alarmAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun formatToUTC(zonedDateTime: ZonedDateTime): ZonedDateTime {
        return zonedDateTime
            .withZoneSameInstant(java.time.ZoneOffset.UTC)
            .withSecond(0)
            .withNano(0)
    }

    private fun setupNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationsFlow.collectLatest { notifications ->
                    if (notifications.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                        alarmAdapter.submitList(notifications)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            getBackButtonDisposable()
        )
    }

    private fun getBackButtonDisposable() = binding.btnBack.clicks()
        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
        .subscribe {
            goBack()
        }
}