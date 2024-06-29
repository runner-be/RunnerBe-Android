package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentImageDetailBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import kotlinx.coroutines.launch

class ImageDetailFragment: BaseFragment<FragmentImageDetailBinding>(R.layout.fragment_image_detail) {

    private val viewModel: ImageDetailViewModel by viewModels<ImageDetailViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.actions.collect(::handleAction)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.currentPageNumber.collect {
                    binding.viewPager.setCurrentItem(it, true)
                }
            }
        }
        binding.viewPager.isUserInputEnabled = false
    }

    private fun handleAction(action: ImageDetailAction) {
        when(action) {
            is ImageDetailAction.MoveToBack -> { navPopStack() }
        }
    }
}