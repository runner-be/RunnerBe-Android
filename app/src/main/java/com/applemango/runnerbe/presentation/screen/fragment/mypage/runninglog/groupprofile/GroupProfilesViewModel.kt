package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.presentation.model.JoinedRunnerModel
import com.applemango.runnerbe.usecaseImpl.runninglog.GetJoinedRunnersUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase.PostStampParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class GroupProfilesViewModel @Inject constructor(
    private val getJoinedRunnersUseCase: GetJoinedRunnersUseCase,
    private val writeStampToJoinedRunnerUseCase: WriteStampToJoinedRunnerUseCase,
    private val joinedRunnerMapper: JoinedRunnerMapper,
): ViewModel() {
    private val runnerInfo = MutableStateFlow<Pair<Int, Int>?>(null)
    private val lastSelectedUserId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val joinedRunnerListFlow: Flow<List<JoinedRunnerModel>> = runnerInfo
        .filterNotNull()
        .flatMapLatest { (first, second) ->
            getJoinedRunnersUseCase(first, second).map {
                it.map { entity ->
                    joinedRunnerMapper.mapToPresentation(entity)
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
                val stampRequest = PostStampParam(
                    targetUserId,
                    stampCode
                )
                /**
                 * TODO
                 * @author Loki
                 * 스탬프 부여 성공 여부에 따라 피드백 추가하기
                 */
                writeStampToJoinedRunnerUseCase(userId, gatheringId, stampRequest)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
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