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
import kotlinx.coroutines.launch

class CreatorFragment: BaseFragment<FragmentCreatorsBinding>(R.layout.fragment_creators) {

    private val viewModel : CreatorViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.creatorRecyclerView.layoutManager = GridLayoutManager(context, 2)
        context?.let {
            binding.creatorRecyclerView.addItemDecoration(
                RecyclerViewGridItemDeco(
                it,
                58,
                41,
                false,
                2
            )
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.collect {
                when(it) {
                    is CreatorAction.MoveToBack -> navPopStack()
                }
            }
        }
    }
}