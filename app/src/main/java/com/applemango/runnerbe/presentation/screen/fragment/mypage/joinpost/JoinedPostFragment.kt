package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentJoinPostBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.mypage.MyPageViewModel
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.BottomSpaceItemDecoration
import com.applemango.runnerbe.util.recyclerview.RightSpaceItemDecoration
import com.applemango.runnerbe.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class JoinedPostFragment : BaseFragment<FragmentJoinPostBinding>(R.layout.fragment_join_post) {

    private val navArgs: JoinedPostFragmentArgs by navArgs()

    @Inject
    lateinit var joinedPostAdapter: JoinedPostAdapter

    private val viewModel: JoinedPostViewModel by viewModels()

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

    private fun updateWithNavArgs(args: JoinedPostFragmentArgs) {
        viewModel.updateTargetUserId(args.targetUserId)
        viewModel.updateTargetNickname(args.nickname)
    }

    private fun setupJoinedPostFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.targetJoinedPostFlow.collectLatest { posts ->
                    joinedPostAdapter.submitList(posts)
                }
            }
        }
    }

    private fun initJoinedPostAdapter() {
        binding.rcvJoinedPost.apply {
            adapter = joinedPostAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(BottomSpaceItemDecoration(12.dpToPx(context)))
        }
    }
}