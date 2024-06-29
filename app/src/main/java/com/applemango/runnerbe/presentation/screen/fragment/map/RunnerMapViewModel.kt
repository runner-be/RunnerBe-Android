package com.applemango.runnerbe.presentation.screen.fragment.map

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.network.request.GetRunningListRequest
import com.applemango.runnerbe.data.network.response.GetRunningListResponse
import com.applemango.runnerbe.data.vo.MapFilterData
import com.applemango.runnerbe.domain.usecase.post.GetRunningListUseCase
import com.applemango.runnerbe.presentation.model.PriorityFilterTag
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.presentation.state.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunnerMapViewModel @Inject constructor(
    private val getRunningListUseCase: GetRunningListUseCase
) : ViewModel() {

    val postList: ObservableArrayList<Posting> = ObservableArrayList()
    private val pageSize = 10
    var isEndPage = false
    var coordinator: LatLng = LatLng(37.5666805, 126.9784147) //서울시청 디폴트

    private val _listUpdateUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val listUpdateUiState get() : MutableStateFlow<UiState> = _listUpdateUiState

    val refreshThisLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var refreshCount = 0
    val filterRunningTag: MutableStateFlow<RunningTag> = MutableStateFlow(RunningTag.All)
    private var preFilterRunningTag: RunningTag = filterRunningTag.value
    val filterPriorityTag: MutableStateFlow<PriorityFilterTag> =
        MutableStateFlow(PriorityFilterTag.BY_DISTANCE)
    private var prePriorityTag = filterPriorityTag.value
    val includeFinish: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private var preIncludeFinish = includeFinish.value
    val filterData: MutableStateFlow<MapFilterData> =
        MutableStateFlow(MapFilterData("A", "N", 0, 100))
    private var preFilterData = filterData.value
    private val isRefresh: StateFlow<Int> = combine(
        filterRunningTag,
        filterPriorityTag,
        includeFinish,
        filterData
    ) { currentRunningTag: RunningTag, currentPriorityTag: PriorityFilterTag, currentIncludeFinish: Boolean, currentMapFilterData: MapFilterData ->
        val result =
            preFilterRunningTag != currentRunningTag || prePriorityTag != currentPriorityTag || preIncludeFinish != currentIncludeFinish || preFilterData != currentMapFilterData
        preFilterRunningTag = currentRunningTag
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

    fun refresh() {
        postList.clear()
        val userId = RunnerBeApplication.mTokenPreference.getUserId()
        getRunningList(if (userId > 0) userId else null, isRefresh = true)
    }

    fun setFilter(gender: String?, jobTag: String?, minAge: Int? = 0, maxAge: Int?) {
        filterData.value = MapFilterData(gender ?: "A", jobTag ?: "N", minAge ?: 0, maxAge ?: 100)
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

    fun getRunningList(userId: Int?, isRefresh: Boolean = false) = viewModelScope.launch {
        val request = GetRunningListRequest(
            userLat = coordinator.latitude,
            userLng = coordinator.longitude,
            jobFilter = filterData.value.jobTag,
            gender = filterData.value.genderTag,
            distanceFilter = "N",
            minAge = if (filterData.value.minAge == 0) "N" else filterData.value.minAge.toString(),
            maxAge = if (filterData.value.maxAge > 65) "N" else filterData.value.maxAge.toString(),
            priorityFilter = filterPriorityTag.value.tag,
            afterPartyFilter = "A", // TODO 필터 추가
            userId = userId,
            whetherEnd = if (includeFinish.value) "Y" else "N",
            pageSize = pageSize,
            page = postList.size / pageSize + 1
        )
        getRunningListUseCase(filterRunningTag.value, request).collect {
            if (it is CommonResponse.Success<*> && it.body is GetRunningListResponse) {
                if (it.body.isSuccess) {
                    if (isRefresh) postList.clear()
                    isEndPage = it.body.runningList.size < pageSize
                    it.body.runningList.forEach { post ->
                        Log.e(post.postId.toString(), post.profileUrlList.toString())
                        if (!postList.contains(post)) postList.add(post)
                    }
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
}

sealed class RunnerMapAction {
    object MoveToWrite : RunnerMapAction()
    data class ShowSelectListDialog(
        val list: List<SelectItemParameter>
    ) : RunnerMapAction()
    data class MoveToRunningFilter(val filterData: MapFilterData): RunnerMapAction()
}