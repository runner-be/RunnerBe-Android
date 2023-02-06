package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import com.applemango.runnerbe.domain.usecase.post.GetAfterBookmarkListUseCase
import com.applemango.runnerbe.domain.usecase.post.GetBeforeBookmarkListUseCase
import com.applemango.runnerbe.domain.usecase.post.GetHolidayBookmarkListUseCase
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val getBeforeBookmarkListUseCase: GetBeforeBookmarkListUseCase,
    private val getAfterBookmarkListUseCase: GetAfterBookmarkListUseCase,
    private val getHolidayBookmarkListUseCase: GetHolidayBookmarkListUseCase
): ViewModel() {

    val radioChecked : MutableStateFlow<Int> = MutableStateFlow(R.id.beforeTab)
    val bookmarkList : ObservableArrayList<Posting> = ObservableArrayList()

    fun getBookmarkList(runningTag : String) = viewModelScope.launch {
        val tag = RunningTag.getByTag(runningTag)
        when(tag) {
            RunningTag.After -> {
                getAfterBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
            RunningTag.Holiday -> {
                getHolidayBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
            else -> { //혹시 모를 다른 것들은 다 출근 전으로...
                getBeforeBookmarkListUseCase(userId = RunnerBeApplication.mTokenPreference.getUserId())
            }
        }.collect {
            if(it is CommonResponse.Success<*> && it.body is GetBookmarkResponse) {
                bookmarkList.clear()
                bookmarkList.addAll(it.body.result.bookMarkList)
            }
        }
    }
}