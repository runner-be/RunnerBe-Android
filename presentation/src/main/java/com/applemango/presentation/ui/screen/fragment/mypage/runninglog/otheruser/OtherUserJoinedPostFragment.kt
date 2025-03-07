package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.presentation.ui.screen.deco.RecyclerViewItemDeco
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.util.dpToPx
import com.applemango.presentation.util.recyclerview.BottomSpaceItemDecoration
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentOtherUserJoinedPostBinding
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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
        initListeners()
        initJoinedPostAdapter()
        setupJoinedPostFlow()
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            getBackBtnDisposable()
        )
    }

    private fun getBackBtnDisposable() = binding.btnBack.clicks()
        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
        .subscribe {
            goBack()
        }

    private fun updateWithNavArgs(args: OtherUserJoinedPostFragmentArgs) {
        viewModel.getJoinedPostList(args.targetUserId)
        viewModel.updateTargetNickname(args.nickname)
    }

    private fun setupJoinedPostFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postList.collectLatest { posts ->
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
                navigate(
                    OtherUserJoinedPostFragmentDirections.actionOtherUserJoinedRunningFragmentToPostDetailFragment(posting)
                )
            }
            addItemDecoration(BottomSpaceItemDecoration(12.dpToPx(context)))
        }
    }
}