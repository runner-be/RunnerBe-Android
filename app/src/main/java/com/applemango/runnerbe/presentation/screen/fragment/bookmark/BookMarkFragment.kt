package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentBookMarkBinding
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookMarkFragment : BaseFragment<FragmentBookMarkBinding>(R.layout.fragment_book_mark) {

    private val viewModel: BookMarkViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        observeBind()
        context?.let {
            binding.bookmarkRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
    }

    private fun getList(tagType: Int) {
        val tag = when (tagType) {
            R.id.beforeTab -> RunningTag.Before
            R.id.afterTab -> RunningTag.After
            R.id.holidayTab -> RunningTag.Holiday
            else -> RunningTag.All
        }
        viewModel.getBookmarkList(tag.tag)
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.radioChecked.collect {
                getList(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mainViewModel.bookmarkPost.collect {
                getList(viewModel.radioChecked.value)
            }
        }
    }

}