package com.applemango.presentation.ui.screen.fragment.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.post.GetPostsUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserPaceUseCase
import com.applemango.presentation.ui.mapper.PostingMapper
import com.applemango.presentation.ui.model.MapFilterModel
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.type.AfterPartyTag
import com.applemango.presentation.ui.model.type.Pace
import com.applemango.presentation.ui.model.type.PriorityFilterTag
import com.applemango.presentation.ui.model.type.RunningTag
import com.applemango.presentation.ui.screen.dialog.selectitem.SelectItemParameter
import com.applemango.presentation.ui.state.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunnerMapViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserPaceUseCase: GetUserPaceUseCase,
    private val postingMapper: PostingMapper,
) : ViewModel() {

    private val _userId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    private val _userPace: MutableStateFlow<Pace?> = MutableStateFlow(null)
    val userPace: StateFlow<Pace?> get() = _userPace.asStateFlow()

    val postList: MutableStateFlow<List<PostingModel>> = MutableStateFlow(emptyList())
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
    val filterData: MutableStateFlow<MapFilterModel> =
        MutableStateFlow(
            MapFilterModel(
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
    ) { currentRunningTag: RunningTag, currentAfterPartyTag: AfterPartyTag, currentPriorityTag: PriorityFilterTag, currentIncludeFinish: Boolean, currentMapFilterModel: MapFilterModel ->
        val result =
            preFilterRunningTag != currentRunningTag
                    || preFilterAfterPartyTag != currentAfterPartyTag
                    || prePriorityTag != currentPriorityTag
                    || preIncludeFinish != currentIncludeFinish
                    || preFilterData != currentMapFilterModel
        preFilterRunningTag = currentRunningTag
        preFilterAfterPartyTag = currentAfterPartyTag
        prePriorityTag = currentPriorityTag
        preIncludeFinish = currentIncludeFinish
        preFilterData = currentMapFilterModel
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

    fun fetchUserId() {
        viewModelScope.launch {
            _userId.value = getUserIdUseCase()
        }
    }

    fun fetchUserPace() {
        viewModelScope.launch {
            val paceString = getUserPaceUseCase()
            _userPace.value = Pace.getPaceByName(paceString)
        }
    }

    fun getRunningList(isRefresh: Boolean = false) = viewModelScope.launch {
        val request = GetPostsUseCase.GetRunningListParam(
            userLat = coordinator.latitude,
            userLng = coordinator.longitude,
            paceFilter = filterData.value.paceTags.joinToString(","),
            jobFilter = filterData.value.jobTag,
            gender = filterData.value.genderTag,
            distanceFilter = "N",
            minAge = if (filterData.value.minAge == 0) "N" else filterData.value.minAge.toString(),
            maxAge = if (filterData.value.maxAge > 65) "N" else filterData.value.maxAge.toString(),
            priorityFilter = filterPriorityTag.value.tag,
            afterPartyFilter = filterData.value.afterPartyTag,
            whetherEnd = if (includeFinish.value) "Y" else "N",
            pageSize = pageSize,
            page = if (isRefresh) 1 else postList.value.size / pageSize + 1
        )
        getPostsUseCase(filterRunningTag.value.tag, request).collect {
            isEndPage = it.size < pageSize
            postList.value = it.map { posting ->
                postingMapper.mapToPresentation(posting)
            }
        }
    }

    fun refresh() {
        postList.value = emptyList()
        getRunningList(isRefresh = true)
    }

    fun updatePostBookmark(post: PostingModel) {
        val postList: MutableList<PostingModel> = postList.value.toMutableList()
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
        filterData.value = MapFilterModel(
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
        viewModelScope.launch {
            _actions.emit(
                RunnerMapAction.ShowSelectListDialog(
                    listOf(
                        SelectItemParameter("") {
                            filterPriorityTag.value = PriorityFilterTag.BY_DISTANCE
                        },
                        SelectItemParameter("") {
                            filterPriorityTag.value = PriorityFilterTag.NEWEST
                        })
                )
            )
        }
    }

    fun runningTagClicked() {
        viewModelScope.launch {
            _actions.emit(
                RunnerMapAction.ShowSelectListDialog(
                    listOf(
                        SelectItemParameter("") {
                            filterRunningTag.value = RunningTag.All
                        },
                        SelectItemParameter("") {
                            filterRunningTag.value = RunningTag.Before
                        },
                        SelectItemParameter("") {
                            filterRunningTag.value = RunningTag.After
                        },
                        SelectItemParameter("") {
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
        data object MoveToWrite : RunnerMapAction()
        data class ShowSelectListDialog(
            val list: List<SelectItemParameter>
        ) : RunnerMapAction()

        data class MoveToRunningFilter(val filterData: MapFilterModel) : RunnerMapAction()
    }
}