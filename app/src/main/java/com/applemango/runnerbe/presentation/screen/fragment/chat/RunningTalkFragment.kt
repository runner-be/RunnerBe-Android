package com.applemango.runnerbe.presentation.screen.fragment.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Room
import com.applemango.runnerbe.databinding.FragmentRunningTalkBinding
import com.applemango.runnerbe.presentation.model.listener.RoomClickListener
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunningTalkFragment: BaseFragment<FragmentRunningTalkBinding>(R.layout.fragment_running_talk) {

    private val viewModel : RunningTalkViewModel by viewModels()

    @Inject
    lateinit var talkAdapter: RunningTalkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initRunningTalkRecyclerView()
        setupRunningTalk()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRunningTalkList()
    }

    private fun setupRunningTalk() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.roomList.collectLatest { list ->
                    talkAdapter.submitList(list)
                }
            }
        }
    }

    private fun initRunningTalkRecyclerView() {
        binding.rcvRunningTalk.apply {
            adapter = talkAdapter.apply {
                setRoomClickListener(object: RoomClickListener {
                    override fun moveToRunningTalkRoom(item: Room) {
                        navigate(MainFragmentDirections.actionMainFragmentToRunningTalkDetailFragment(item.roomId, item.repUserName))
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
        }
    }
}