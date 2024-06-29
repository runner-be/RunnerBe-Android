package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentMyPostBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainFragmentDirections
import com.applemango.runnerbe.presentation.screen.fragment.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPostFragment : BaseFragment<FragmentMyPostBinding>(R.layout.fragment_my_post) {

    private val myPageViewModel: MyPageViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val viewModel : MyPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        binding.viewModel = viewModel
        context?.let {
            binding.postRecycler.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.action.collect {
                when(it) {
                    is MyPostActions.MoveToWrite -> {
                        checkAdditionalUserInfo {
                            navigate(MainFragmentDirections.actionMainFragmentToRunningWriteFragment())
                        }
                    }
                    is MyPostActions.MoveToMyPostAttendanceSee -> {
                        navigate(
                            MainFragmentDirections.actionMainFragmentToMyPostAttendanceSeeFragment(
                                it.post.runnerList?.toTypedArray() ?: arrayOf()
                            )
                        )
                    }
                    is MyPostActions.MoveToAttendanceManage -> {
                        navigate(
                            MainFragmentDirections.actionMainFragmentToMyPostAttendanceAccessionFragment(
                                it.post.postId,
                                it.post.runnerList?.toTypedArray() ?: arrayOf()
                            )
                        )
                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.changedBookMarkPost.collect {
                    myPageViewModel.postUpdate(it)
                }
            }
        }
    }
}