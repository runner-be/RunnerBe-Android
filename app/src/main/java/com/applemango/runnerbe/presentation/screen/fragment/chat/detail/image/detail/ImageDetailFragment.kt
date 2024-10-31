package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentImageDetailBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ImageDetailFragment: BaseFragment<FragmentImageDetailBinding>(R.layout.fragment_image_detail) {

    private val viewModel: ImageDetailViewModel by viewModels()
    private val navArgs: ImageDetailFragmentArgs by navArgs()

    @Inject
    lateinit var imageViewPagerAdapter: ImageDetailViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initListeners()
        initImageArgs(navArgs)
        initImageViewPager()
        setupImageList()
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.tvBack.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    goBack()
                }
        )
    }

    private fun setupImageList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageList.collectLatest { imageUriList ->
                    imageViewPagerAdapter.submitList(imageUriList)
                }
            }
        }
    }

    private fun initImageArgs(navArgs: ImageDetailFragmentArgs) {
        with(binding) {
            topTxt.text = navArgs.title
            val imageList = navArgs.images.map { Uri.parse(it) }
            viewModel.updateImageList(imageList)
        }
    }

    private fun initImageViewPager() {
        binding.vpImage.apply {
            adapter = imageViewPagerAdapter
            setCurrentItem(0, false)
        }
    }
}