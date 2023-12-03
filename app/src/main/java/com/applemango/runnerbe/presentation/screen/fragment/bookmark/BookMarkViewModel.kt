package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import com.applemango.runnerbe.domain.usecase.bookmark.*
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.model.listener.BookMarkClickListener
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val getAllDayBookmarkListUseCase: GetAllDayBookmarkListUseCase,
    private val getBeforeBookmarkListUseCase: GetBeforeBookmarkListUseCase,
    private val getAfterBookmarkListUseCase: GetAfterBookmarkListUseCase,
    private val getHolidayBookmarkListUseCase: GetHolidayBookmarkListUseCase,
    private val bookMarkStatusChangeUseCase: BookMarkStatusChangeUseCase
): ViewModel() {

    val radioChecked : MutableStateFlow<Int> = MutableStateFlow(R.id.allTab)
    val bookmarkList : ObservableArrayList<Posting> = ObservableArrayList()

    fun getBookmarkList(runningTag : String) = viewModelScope.launch {
        val tag = RunningTag.getByTag(runningTag)
        when(tag) {
            RunningTag.Before -> {
                getBeforeBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
            RunningTag.After -> {
                getAfterBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
            RunningTag.Holiday -> {
                getHolidayBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
            else -> { //혹시 모를 다른 것들은 다 출근 전으로...
                getAllDayBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
        }.collect {
            if(it is CommonResponse.Success<*> && it.body is GetBookmarkResponse && it.body.result.bookMarkList != null) {
                bookmarkList.clear()
                bookmarkList.addAll(it.body.result.bookMarkList!!)
            }
        }
    }

    fun getChangeBookMarkStatusListener() = object : BookMarkClickListener {
        override fun onBookMarkClick(post: Posting) {
            bookmarkStatusChange(post)
        }

        override fun onClick(post: Posting) {

        }
    }

    fun bookmarkStatusChange(post: Posting) = viewModelScope.launch {
        bookMarkStatusChangeUseCase(
            RunnerBeApplication.mTokenPreference.getUserId(),
            post.postId,
            if (!post.bookmarkCheck()) "Y" else "N"
        ).collect {
            when(it) {
                is CommonResponse.Success<*> -> {
                    if(it.body is BaseResponse && it.body.isSuccess) {
                        post.bookMark = if(post.bookmarkCheck()) 0 else 1
                        postChange(post)
                    }
                }
            }
        }
    }

    fun postChange(posting: Posting) {
        val index = bookmarkList.indexOf(posting)
        if(index != -1 ) bookmarkList[index] = posting
    }
}