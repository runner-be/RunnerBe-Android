package com.applemango.runnerbe.presentation.screen.fragment.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.request.GetRunningListRequest
import com.applemango.runnerbe.data.network.response.GetRunningListResponse
import com.applemango.runnerbe.data.vo.MapFilterData
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.domain.usecase.post.GetPostsUseCase
import com.applemango.runnerbe.presentation.model.AfterPartyTag
import com.applemango.runnerbe.presentation.model.PriorityFilterTag
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunnerMapViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    val postList: MutableStateFlow<List<Posting>> = MutableStateFlow(emptyList())
    val panelTop: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val pageSize = 10
    private var isEndPage = false
    var coordinator: LatLng = LatLng(37.5666805, 126.9784147) //서울시청 디폴트

    private val _listUpdateUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val listUpdateUiState get() : MutableStateFlow<UiState> = _listUpdateUiState

    val refreshThisLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var refreshCount = 0
    val filterRunningTag: MutableStateFlow<RunningTag> = MutableStateFlow(RunningTag.All)
    private var preFilterRunningTag: RunningTag? = filterRunningTag.value

    private val filterAfterPartyTag: MutableStateFlow<AfterPartyTag> =
        MutableStateFlow(AfterPartyTag.ALL)
    private var preFilterAfterPartyTag: AfterPartyTag? = filterAfterPartyTag.value

    val filterPriorityTag: MutableStateFlow<PriorityFilterTag> =
        MutableStateFlow(PriorityFilterTag.NEWEST)
    private var prePriorityTag = filterPriorityTag.value
    val includeFinish: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private var preIncludeFinish = includeFinish.value
    val filterData: MutableStateFlow<MapFilterData> =
        MutableStateFlow(
            MapFilterData(
                listOf(
                    Pace.ALL,
                    Pace.BEGINNER,
                    Pace.AVERAGE,
                    Pace.HIGH,
                    Pace.MASTER
                ), "A", "A", "N", 0, 100
            )
        )
    private var preFilterData = filterData.value
    private val isRefresh: StateFlow<Int> = combine(
        filterRunningTag.filterNotNull(),
        filterAfterPartyTag.filterNotNull(),
        filterPriorityTag,
        includeFinish,
        filterData
    ) { currentRunningTag: RunningTag, currentAfterPartyTag: AfterPartyTag, currentPriorityTag: PriorityFilterTag, currentIncludeFinish: Boolean, currentMapFilterData: MapFilterData ->
        val result =
            preFilterRunningTag != currentRunningTag
                    || preFilterAfterPartyTag != currentAfterPartyTag
                    || prePriorityTag != currentPriorityTag
                    || preIncludeFinish != currentIncludeFinish
                    || preFilterData != currentMapFilterData
        preFilterRunningTag = currentRunningTag
        preFilterAfterPartyTag = currentAfterPartyTag
        prePriorityTag = currentPriorityTag
        preIncludeFinish = currentIncludeFinish
        preFilterData = currentMapFilterData
        if (result) ++refreshCount
        else refreshCount
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = 0
    )

    private val _actions: MutableSharedFlow<RunnerMapAction> = MutableSharedFlow()
    val actions: SharedFlow<RunnerMapAction> get() = _actions

    init {
        viewModelScope.launch {
            isRefresh.collect { refresh() }
        }
    }

    fun getRunningList(userId: Int?, isRefresh: Boolean = false) = viewModelScope.launch {
        // TODO  테스트 용도이므로 종료 시 삭제
        val iCoordinator = if (RunnerBeApplication.mTokenPreference.getIsTestMode()) {
            LatLng(37.3419817000, 127.0940174000)
        } else {
            coordinator
        }
        val request = GetRunningListRequest(
            userLat = iCoordinator.latitude,
            userLng = iCoordinator.longitude,
            paceFilter = filterData.value.paceTags.joinToString(","),
            jobFilter = filterData.value.jobTag,
            gender = filterData.value.genderTag,
            distanceFilter = "N",
            minAge = if (filterData.value.minAge == 0) "N" else filterData.value.minAge.toString(),
            maxAge = if (filterData.value.maxAge > 65) "N" else filterData.value.maxAge.toString(),
            priorityFilter = filterPriorityTag.value.tag,
            afterPartyFilter = filterData.value.afterPartyTag,
            userId = userId,
            whetherEnd = if (includeFinish.value) "Y" else "N",
            pageSize = pageSize,
            page = if (isRefresh) 1 else postList.value.size / pageSize + 1
        )
        getPostsUseCase(filterRunningTag.value, request).collect {
            if (it is CommonResponse.Success<*> && it.body is GetRunningListResponse) {
                if (it.body.isSuccess) {
                    isEndPage = it.body.runningList.size < pageSize
                    val newList = it.body.runningList
                    postList.value = newList
                }
            }
            _listUpdateUiState.emit(
                when (it) {
                    is CommonResponse.Success<*> -> UiState.Success(it.code)
                    is CommonResponse.Failed -> {
                        if (it.code >= 999) UiState.NetworkError
                        else UiState.Failed(it.message)
                    }

                    is CommonResponse.Loading -> UiState.Loading
                    else -> UiState.Empty
                }
            )
        }
    }

    fun refresh() {
        postList.value = emptyList()
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        getRunningList(userId, isRefresh = true)
    }

    fun updatePostBookmark(post: Posting) {
        val postList: MutableList<Posting> = postList.value.toMutableList()
        val parsedPostList = postList.map { item ->
            if (item.postId == post.postId) {
                val prevBookmark = if (post.bookMark == 1) 0 else 1
                item.copy(bookMark = prevBookmark)
            } else {
                item
            }
        }
        this.postList.value = parsedPostList
    }

    fun setFilter(
        paces: List<Pace>,
        gender: String?,
        afterParty: String?,
        jobTag: String?,
        minAge: Int? = 0,
        maxAge: Int?
    ) {
        filterData.value = MapFilterData(
            paces,
            gender ?: "A",
            afterParty ?: "all",
            jobTag ?: "N",
            minAge ?: 0,
            maxAge ?: 100
        )
    }

    fun writeClicked() {
        viewModelScope.launch {
            _actions.emit(RunnerMapAction.MoveToWrite)
        }
    }

    fun priorityTagClicked() {
        val resources = RunnerBeApplication.instance.resources
        viewModelScope.launch {
            _actions.emit(
                RunnerMapAction.ShowSelectListDialog(
                    listOf(
                        SelectItemParameter(resources.getString(PriorityFilterTag.BY_DISTANCE.getTagNameResource())) {
                            filterPriorityTag.value = PriorityFilterTag.BY_DISTANCE
                        },
                        SelectItemParameter(resources.getString(PriorityFilterTag.NEWEST.getTagNameResource())) {
                            filterPriorityTag.value = PriorityFilterTag.NEWEST
                        })
                )
            )
        }
    }

    fun runningTagClicked() {
        val resources = RunnerBeApplication.instance.resources
        viewModelScope.launch {
            _actions.emit(
                RunnerMapAction.ShowSelectListDialog(
                    listOf(
                        SelectItemParameter(resources.getString(RunningTag.All.getTagNameResource())) {
                            filterRunningTag.value = RunningTag.All
                        },
                        SelectItemParameter(resources.getString(RunningTag.Before.getTagNameResource())) {
                            filterRunningTag.value = RunningTag.Before
                        },
                        SelectItemParameter(resources.getString(RunningTag.After.getTagNameResource())) {
                            filterRunningTag.value = RunningTag.After
                        },
                        SelectItemParameter(resources.getString(RunningTag.Holiday.getTagNameResource())) {
                            filterRunningTag.value = RunningTag.Holiday
                        }
                    )
                )
            )
        }
    }

    fun filterClicked() {
        viewModelScope.launch {
            _actions.emit(RunnerMapAction.MoveToRunningFilter(filterData.value))
        }
    }

    sealed class RunnerMapAction {
        object MoveToWrite : RunnerMapAction()
        data class ShowSelectListDialog(
            val list: List<SelectItemParameter>
        ) : RunnerMapAction()

        data class MoveToRunningFilter(val filterData: MapFilterData) : RunnerMapAction()
    }
}