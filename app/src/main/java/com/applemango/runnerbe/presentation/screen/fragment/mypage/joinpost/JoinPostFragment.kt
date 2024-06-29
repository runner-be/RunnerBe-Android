package com.applemango.runnerbe.presentation.screen.fragment.mypage.joinpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentJoinPostBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.mypage.MyPageViewModel

class JoinPostFragment : BaseFragment<FragmentJoinPostBinding>(R.layout.fragment_join_post) {

    private val viewModel: MyPageViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        context?.let {
            binding.runningRecycler.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
        binding.emptyButton.setOnClickListener {
            viewModel.setTab(0)
        }
    }
}