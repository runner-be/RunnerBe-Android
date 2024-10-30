package com.applemango.runnerbe.presentation.screen.fragment.map.filter

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.presentation.model.AfterPartyTag
import com.applemango.runnerbe.presentation.model.GenderTag
import com.applemango.runnerbe.presentation.model.JobButtonId
import com.applemango.runnerbe.presentation.model.listener.PaceSelectListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.PaceSelectItem
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.initPaceInfoListWithAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RunningFilterViewModel : ViewModel() {
    val paceList: MutableStateFlow<List<PaceSelectItem>> =
        MutableStateFlow(initPaceInfoListWithAll())

    val paceCheckedList: MutableStateFlow<List<Pace>> = MutableStateFlow(emptyList())
    val genderRadioChecked: MutableStateFlow<Int> = MutableStateFlow(R.id.allTab)
    val afterPartyRadioChecked: MutableStateFlow<Int> = MutableStateFlow(R.id.rb_all_after_party)
    val jobRadioChecked: MutableStateFlow<Int> = MutableStateFlow(-1)
    val isAllAgeChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val recruitmentStartAge: MutableStateFlow<Int> = MutableStateFlow(20)
    val recruitmentEndAge: MutableStateFlow<Int> = MutableStateFlow(40)
    val recruitmentAge = combine(recruitmentStartAge, recruitmentEndAge) { start, end ->
        RunnerBeApplication.ApplicationContext().resources.getString(
            R.string.display_recruitment_age_setting,
            start.toString(),
            end.toString()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = ""
    )

    val actions: MutableSharedFlow<RunningFilterAction> = MutableSharedFlow()
    fun refresh() {
        paceCheckedList.value = listOf(Pace.ALL)
        genderRadioChecked.value = R.id.allTab
        afterPartyRadioChecked.value = R.id.rb_all_after_party
        isAllAgeChecked.value = true
        recruitmentStartAge.value = 20
        recruitmentEndAge.value = 40
        jobRadioChecked.value = -1
    }

    fun backClicked() {
        viewModelScope.launch {
            actions.emit(RunningFilterAction.MoveToBack(getFilterToBundle()))
        }
    }

    fun getPaceInfoCheckSelectListener(): PaceSelectListener = object : PaceSelectListener {
        override fun itemClick(paceSelectItem: PaceSelectItem) {
            val isAllChecked = paceList.value.drop(1).all { it.isSelected }

            val updatedList = if (isAllChecked) {
                if (paceSelectItem.pace == Pace.ALL) {
                    paceList.value.map {
                        it.copy(isSelected = false)
                    }
                } else {
                    paceList.value.map { item ->
                        if (item.pace == Pace.ALL || item.pace == paceSelectItem.pace) {
                            item.copy(isSelected = false)
                        } else item
                    }
                }
            } else {
                if (paceSelectItem.pace == Pace.ALL) {
                    paceList.value.map {
                        it.copy(isSelected = true)
                    }
                } else {
                    paceList.value.map { item ->
                        if (item.pace == paceSelectItem.pace) {
                            item.copy(isSelected = !item.isSelected)
                        } else item
                    }
                }
            }
            val finalList = if (updatedList.drop(1).all { it.isSelected }) {
                updatedList.map {
                    if (it.pace == Pace.ALL) {
                        it.copy(isSelected = true)
                    } else it
                }
            } else updatedList

            paceList.value = finalList
        }
    }

    private fun getPaceTags(): List<Pace> {
        return paceList.value
            .filter { it.isSelected }
            .map { it.pace }
            .also {
                Log.e("getPaceTags", "when back pressed : $it")
            }
    }

    fun setPaceTags(paceTags: List<Pace>) {
        paceCheckedList.value = paceTags

        val updatedList = if (paceTags.contains(Pace.ALL)) {
            paceList.value.map { item ->
                item.copy(isSelected = true)
            }
        } else {
            paceList.value.map { item ->
                if (paceTags.contains(item.pace)) {
                    item.copy(isSelected = true)
                } else item
            }
        }

        paceList.value = updatedList
    }

    private fun getGenderTag(): String = when (genderRadioChecked.value) {
        R.id.maleTab -> GenderTag.MALE
        R.id.femaleTab -> GenderTag.FEMALE
        else -> GenderTag.ALL
    }.tag

    fun setGenderTag(genderTag: String) {
        genderRadioChecked.value = when (genderTag) {
            GenderTag.MALE.tag -> R.id.maleTab
            GenderTag.FEMALE.tag -> R.id.femaleTab
            else -> R.id.allTab
        }
    }

    private fun getAfterPartyTag(): String = when (afterPartyRadioChecked.value) {
        R.id.rb_no_after_party -> AfterPartyTag.NO
        R.id.rb_yes_after_party -> AfterPartyTag.YES
        else -> AfterPartyTag.ALL
    }.tag

    fun setAfterPartyTag(afterParty: String) {
        afterPartyRadioChecked.value = when (afterParty) {
            AfterPartyTag.NO.tag -> R.id.rb_no_after_party
            AfterPartyTag.YES.tag -> R.id.rb_yes_after_party
            else -> R.id.rb_all_after_party
        }
    }

    private fun getJobTag(): String? = JobButtonId.findById(jobRadioChecked.value)?.job

    fun setJobTag(jobTag: String) {
        jobRadioChecked.value = JobButtonId.findByJob(jobTag)?.id ?: -1
    }

    private fun getFilterToBundle(): Bundle = Bundle().apply {
        putParcelableArray("paces", getPaceTags().toTypedArray())
        putString("gender", getGenderTag())
        putSerializable("afterParty", getAfterPartyTag())
        putString("job", (getJobTag() ?: "N"))
        putInt("minAge", if (isAllAgeChecked.value) 0 else recruitmentStartAge.value)
        putInt("maxAge", if (isAllAgeChecked.value) 100 else recruitmentEndAge.value)
    }
}

sealed class RunningFilterAction {
    data class MoveToBack(val bundle: Bundle) : RunningFilterAction()
}