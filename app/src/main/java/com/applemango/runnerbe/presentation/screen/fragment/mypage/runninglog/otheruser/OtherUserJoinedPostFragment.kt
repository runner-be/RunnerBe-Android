package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentOtherUserJoinedPostBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.BottomSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OtherUserJoinedPostFragment : BaseFragment<FragmentOtherUserJoinedPostBinding>(R.layout.fragment_other_user_joined_post) {

    private val navArgs: OtherUserJoinedPostFragmentArgs by navArgs()

    @Inject
    lateinit var otherUserJoinedPostAdapter: OtherUserJoinedPostAdapter

    private val viewModel: OtherUserJoinedPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        updateWithNavArgs(navArgs)
        context?.let {
            binding.rcvJoinedPost.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
        initJoinedPostAdapter()
        setupJoinedPostFlow()
    }

    private fun updateWithNavArgs(args: OtherUserJoinedPostFragmentArgs) {
        viewModel.updateTargetUserId(args.targetUserId)
        viewModel.updateTargetNickname(args.nickname)
    }

    private fun setupJoinedPostFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.targetJoinedPostFlow.collectLatest { posts ->
                    otherUserJoinedPostAdapter.submitList(posts)
                }
            }
        }
    }

    private fun initJoinedPostAdapter() {
        binding.rcvJoinedPost.apply {
            adapter = otherUserJoinedPostAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            otherUserJoinedPostAdapter.setOnPostClickListener { posting ->
                // TODO
//                navigate(
//                    OtherUserJoinedPostFragmentDirections.actionJoinPostFragmentToPostDetailFragment(posting)
//                )
            }
            addItemDecoration(BottomSpaceItemDecoration(12.dpToPx(context)))
        }
    }
}