package com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo

import android.content.Context
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.type.Pace
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PaceInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun initPaceInfoList() : List<PaceSelectItem> {
        return listOf(
            PaceSelectItem(
                pace = Pace.BEGINNER,
                paceImageResource = R.drawable.ic_beginner_pace,
                paceName = context.getString(R.string.beginner_pace_runner),
                paceDescription = context.getString(R.string.beginner_pace_runner_description),
                isSelected = false
            ),
            PaceSelectItem(
                pace = Pace.AVERAGE,
                paceImageResource = R.drawable.ic_general_pace,
                paceName = context.getString(R.string.general_pace_runner),
                paceDescription = context.getString(R.string.general_pace_runner_description),
                isSelected = false
            ),
            PaceSelectItem(
                pace = Pace.HIGH,
                paceImageResource = R.drawable.ic_master_pace,
                paceName = context.getString(R.string.master_pace_runner),
                paceDescription = context.getString(R.string.master_pace_runner_description),
                isSelected = false
            ),
            PaceSelectItem(
                pace = Pace.MASTER,
                paceImageResource = R.drawable.ic_grand_matser_pace,
                paceName = context.getString(R.string.grand_matser_pace_runner),
                paceDescription = context.getString(R.string.grand_matser_pace_runner_description),
                isSelected = false
            )
        )
    }

    fun initPaceInfoListWithAll(): List<PaceSelectItem> {
        val allPaceItem = PaceSelectItem(
            pace = Pace.ALL,
            paceImageResource = 0,
            paceName = "페이스 무관",
            paceDescription = "페이스 맞춤형 러너",
            isSelected = false
        )

        return listOf(allPaceItem) + initPaceInfoList()
    }
}