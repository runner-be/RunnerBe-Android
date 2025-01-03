package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.data.network.response.JoinedRunnerResponse
import com.applemango.runnerbe.data.network.response.JoinedRunnerResult
import com.applemango.runnerbe.domain.usecase.runninglog.GetJoinedRunnersUseCase
import com.applemango.runnerbe.domain.usecase.runninglog.WriteStampToJoinedRunnerUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class GroupProfilesViewModel @Inject constructor(
    private val getJoinedRunnersUseCase: GetJoinedRunnersUseCase,
    private val writeStampToJoinedRunnerUseCase: WriteStampToJoinedRunnerUseCase
): ViewModel() {
    private val runnerInfo = MutableStateFlow<Pair<Int, Int>?>(null)
    private val lastSelectedUserId = MutableStateFlow<Int?>(null)

    private val _stampResult = MutableStateFlow<CommonResponse>(CommonResponse.Empty)
    val stampResult: StateFlow<CommonResponse> = _stampResult.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val joinedRunnerListFlow: Flow<List<JoinedRunnerResult>> = runnerInfo
        .filterNotNull()
        .flatMapLatest { (first, second) ->
            getJoinedRunnersUseCase(first, second)
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

    fun postStampToJoinedRunner(userId: Int, gatheringId: Int, stampCode: String) {
        viewModelScope.launch {
            try {
                val targetUserId = requireNotNull(lastSelectedUserId.value) {
                    "선택된 사용자가 없습니다"
                }
                val stampRequest = PostStampRequest(
                    targetUserId,
                    stampCode
                )
                writeStampToJoinedRunnerUseCase(userId, gatheringId, stampRequest)
                    .onStart {
                        _stampResult.value = CommonResponse.Loading
                    }
                    .collect { response ->
                        _stampResult.value = response
                    }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                _stampResult.value = CommonResponse.Failed.getDefaultFailed(e.message)
            }
        }
    }

    fun updateLastSelectedUserId(userId: Int) {
        lastSelectedUserId.value = userId
    }

    fun updateRunnerInfo(userId: Int, logId: Int) {
        runnerInfo.value = Pair(userId, logId)
    }
}