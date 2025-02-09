package com.applemango.presentation.ui.screen.fragment.mypage.calendar.monthly

import androidx.lifecycle.ViewModel
import com.applemango.domain.usecaseImpl.runninglog.GetMonthlyRunningLogsUseCase
import com.applemango.domain.usecaseImpl.user.local.GetUserIdUseCase
import com.applemango.presentation.ui.mapper.MonthlyRunningLogMapper
import com.applemango.presentation.ui.model.MonthlyRunningLogsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MonthlyCalendarViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getMonthlyRunningLogsUseCase: GetMonthlyRunningLogsUseCase,
    private val monthlyRunningLogMapper: MonthlyRunningLogMapper
): ViewModel() {
    val stampStatistic = MutableStateFlow(mapOf(Pair("크루", 4), Pair("개인", 6)))

    private val _targetUserId = MutableStateFlow<Int?>(null)
    val targetUserId: StateFlow<Int?> = _targetUserId.asStateFlow()

    private val _selectedYearMonth = MutableStateFlow(Pair(LocalDate.now().year, LocalDate.now().monthValue))
    val selectedYearMonth: StateFlow<Pair<Int, Int>> = _selectedYearMonth.asStateFlow()

    init {
        _selectedYearMonth.value = Pair(LocalDate.now().year, LocalDate.now().monthValue)
    }

    /**
     * 현재는 내 userId로 내 월간 로그 리스트만 조회하도록 되어 있음
     */
    @ExperimentalCoroutinesApi
    val selectedYearMonthFlow: Flow<MonthlyRunningLogsModel> = selectedYearMonth
        .flatMapLatest { yearMonthPair ->
            try {
                val userId = getUserIdUseCase()
                getMonthlyRunningLogsUseCase(userId, yearMonthPair.first, yearMonthPair.second).map {
                    monthlyRunningLogMapper.mapToPresentation(it)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                emptyFlow()
            }
        }.flowOn(Dispatchers.IO)

    fun updateSelectedYearMonth(year: Int, month: Int) {
        if (year != _selectedYearMonth.value.first || month != _selectedYearMonth.value.second) {
            _selectedYearMonth.value = Pair(year, month)
        }
    }

    fun updateTargetUserId(userId: Int) {
        _targetUserId.value = userId
    }
}