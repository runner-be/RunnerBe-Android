package com.applemango.presentation.ui.screen.fragment.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.type.RunningTag
import com.applemango.presentation.ui.screen.deco.RecyclerViewItemDeco
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.ui.screen.fragment.main.MainViewModel
import com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentBookMarkBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TODO - 필터 변경에 따른 리사이클러뷰 리스트 변경 시 Flow 적용하기
 */
@AndroidEntryPoint
class BookMarkFragment : BaseFragment<FragmentBookMarkBinding>(R.layout.fragment_book_mark) {

    private val viewModel: BookMarkViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels({ requireParentFragment() })

    @Inject
    lateinit var bookmarkAdapter: BookMarkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        observeBind()
        initBookmarkRecyclerView()
        initRadioGroupListener()
        context?.let {
            binding.bookmarkRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getBookmarkList()
    }

    private fun initBookmarkRecyclerView() {
        binding.bookmarkRecyclerView.apply {
            adapter = bookmarkAdapter.apply {
                setBookmarkClickListener(object : JoinedRunningClickListener {
                    override fun logWriteClick(post: PostingModel) {
                    }

                    override fun attendanceSeeClick(post: PostingModel) {
                    }

                    override fun attendanceManageClick(post: PostingModel) {
                    }

                    override fun bookMarkClick(post: PostingModel) {
                        mainViewModel.bookmarkStatusChange(post)
                        viewModel.addOrRemoveBookmarkedPost(post)
                    }

                    override fun postClick(post: PostingModel) {
                    }
                })
            }
            itemAnimator = null
        }
    }

    private fun initRadioGroupListener() {
        binding.rgTag.setOnCheckedChangeListener { _, checkedId ->
            val tag = when(checkedId) {
                R.id.allTab -> RunningTag.All
                R.id.beforeTab -> RunningTag.Before
                R.id.afterTab -> RunningTag.After
                R.id.holidayTab -> RunningTag.Holiday
                else -> throw IllegalStateException("잘못된 러닝 태그입니다.")
            }
            viewModel.updateSelectedTag(tag)
        }
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.filteredBookmark.collectLatest { postingList ->
                        bookmarkAdapter.submitList(postingList)
                    }
                }

                launch {
                    mainViewModel.bookmarkPost.collect { posting ->
                        viewModel.addOrRemoveBookmarkedPost(posting)
                    }
                }
            }
        }
    }
}