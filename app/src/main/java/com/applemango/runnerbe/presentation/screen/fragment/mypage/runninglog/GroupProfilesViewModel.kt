package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.response.JoinedRunnerResponse
import com.applemango.runnerbe.data.network.response.JoinedRunnerResult
import com.applemango.runnerbe.domain.usecase.runninglog.GetJoinedRunnerListUseCase
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.google.android.gms.common.internal.service.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupProfilesViewModel @Inject constructor(
    private val getJoinedRunnerListUseCase: GetJoinedRunnerListUseCase
): ViewModel() {
    private val runnerInfo = MutableStateFlow<Pair<Int, Int>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val joinedRunnerListFlow: Flow<List<JoinedRunnerResult>> = runnerInfo
        .filterNotNull()
        .flatMapLatest { (first, second) ->
            getJoinedRunnerListUseCase(first, second)
        }
        .map { response ->
            when (response) {
                is CommonResponse.Success<*> -> {
                    val result = response.body as? JoinedRunnerResponse
                    (result?.result).orEmpty()
                }
                is CommonResponse.Failed -> {
                    Log.e("joinedRunnerListFlow", " F A I L E D")
                    emptyList<JoinedRunnerResult>()
                }
                else -> {
                    Log.e("joinedRunnerListFlow", " E L S E")
                    emptyList<JoinedRunnerResult>()
                }
            }
        }
        .catch { throwable ->
            throwable.printStackTrace()
        }
        .flowOn(Dispatchers.IO)

    fun updateRunnerInfo(userId: Int, logId: Int) {
        runnerInfo.value = Pair(userId, logId)
    }
}