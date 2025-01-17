package com.applemango.runnerbe.presentation.screen.fragment.mypage.setting.creator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentCreatorsBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewGridItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreatorFragment: BaseFragment<FragmentCreatorsBinding>(R.layout.fragment_creators) {

    private val viewModel : CreatorViewModel by viewModels()

    @Inject lateinit var creatorAdapter: CreatorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initCreatorAdapter()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.collect {
                when(it) {
                    is CreatorAction.MoveToBack -> navPopStack()
                }
            }
        }
        creatorAdapter.submitList(viewModel.creatorList)
    }

    private fun initCreatorAdapter() {
        binding.rcvCreator.apply {
            adapter = creatorAdapter
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    16.dpToPx(context),
                    16.dpToPx(context),
                )
            )
        }
    }
}