package com.applemango.runnerbe.presentation.screen.fragment.map.filter

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.presentation.model.GenderTag
import com.applemango.runnerbe.presentation.model.JobButtonId
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RunningFilterViewModel: ViewModel() {

    val genderRadioChecked : MutableStateFlow<Int> = MutableStateFlow(R.id.allTab)
    val jobRadioChecked : MutableStateFlow<Int> = MutableStateFlow(-1)
    val isAllAgeChecked : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val recruitmentStartAge : MutableStateFlow<Int> = MutableStateFlow(20)
    val recruitmentEndAge : MutableStateFlow<Int> = MutableStateFlow(40)
    val recruitmentAge = combine(recruitmentStartAge, recruitmentEndAge) { start, end ->
        RunnerBeApplication.ApplicationContext().resources.getString(R.string.display_recruitment_age_setting, start.toString(), end.toString())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000L),
        initialValue = ""
    )

    val actions: MutableSharedFlow<RunningFilterAction> = MutableSharedFlow()
    fun refresh() {
        genderRadioChecked.value = R.id.allTab
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

    private fun getGenderTag(): String = when (genderRadioChecked.value) {
        R.id.maleTab -> GenderTag.MALE
        R.id.femaleTab -> GenderTag.FEMALE
        else -> GenderTag.ALL
    }.tag

    fun setGenderTag(genderTag: String) {
        genderRadioChecked.value = when(genderTag) {
            GenderTag.MALE.tag -> R.id.maleTab
            GenderTag.FEMALE.tag -> R.id.femaleTab
            else -> R.id.allTab
        }
    }

    private fun getJobTag(): String? = JobButtonId.findById(jobRadioChecked.value)?.job

    fun setJobTag(jobTag : String) {
        jobRadioChecked.value = JobButtonId.findByJob(jobTag)?.id?:-1
    }

    private fun getFilterToBundle() : Bundle = bundleOf(
        "gender" to getGenderTag(),
        "job" to (getJobTag()?:"N"),
        "minAge" to if(isAllAgeChecked.value) 0 else recruitmentStartAge.value,
        "maxAge" to if(isAllAgeChecked.value) 100 else recruitmentEndAge.value
    )
}

sealed class RunningFilterAction {
    data class MoveToBack(val bundle: Bundle): RunningFilterAction()
}