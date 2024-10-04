package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import androidx.lifecycle.ViewModel
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.domain.usecase.runninglog.GetJoinedRunnerListUseCase
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class GroupProfilesViewModel @Inject constructor(
    private val getJoinedRunnerListUseCase: GetJoinedRunnerListUseCase
): ViewModel() {
    val runnerList = MutableStateFlow(testInitRunners())

    private fun testInitRunners(): List<ProfileItem> {
        val context = RunnerBeApplication.ApplicationContext()
        return listOf(
            ProfileItem(
                null,
                "김민규",
                true,
                StampItem(
                    "available",
                    R.drawable.ic_stamp_1_personal,
                    context.getString(R.string.stamp_1_name),
                    context.getString(R.string.stamp_1_description),
                    true
                )
            ),
            ProfileItem(
                null,
                "박정우가나다라마바사아자차카타파하",
                false,
                null
            ),
            ProfileItem(
                null,
                "조근우",
                false,
                null
            ),
            ProfileItem(
                null,
                "박동환",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
            ProfileItem(
                null,
                "장동호",
                false,
                null
            ),
        )
    }
}