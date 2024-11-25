package com.applemango.runnerbe.presentation.screen.dialog.stamp

import android.os.Parcelable
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem.Companion.unavailableStampItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class StampItem(
    val code: String,
    val image: Int,
    val name: String,
    val description: String,
    val isSelected: Boolean
) : Parcelable {
    companion object {
        val availableStampItem = StampItem(
            "available",
            R.drawable.ic_stamp_available,
            "작성 가능",
            "러닝 기록 존재",
            false
        )

        val unavailableStampItem = StampItem(
            "unavailable",
            R.drawable.ic_stamp_unavailable,
            "터치해서 러닝 스탬프를 찍어볼까요?",
            "러닝 기록 존재하지 않음",
            false
        )

        val otherUserUnavailableStampItem = StampItem(
            "otherUser",
            R.drawable.ic_stamp_other_user_empty,
            "",
            "",
            false
        )
    }
}

fun getStampItemByCode(code: String?) : StampItem {
    if (code == null) return unavailableStampItem

    val stampList = getStampItems()
    return stampList.firstOrNull { it.code == code } ?: unavailableStampItem
}

fun getStampItems(): List<StampItem> {
    val context = RunnerBeApplication.ApplicationContext()
    return listOf(
        StampItem(
            "RUN001",
            R.drawable.ic_stamp_1_personal,
            context.getString(R.string.stamp_1_name),
            context.getString(R.string.stamp_1_description),
            true
        ),
        StampItem(
            "RUN002",
            R.drawable.ic_stamp_2_group,
            context.getString(R.string.stamp_2_name),
            context.getString(R.string.stamp_2_description),
            false
        ),
        StampItem(
            "RUN003",
            R.drawable.ic_stamp_3_social,
            context.getString(R.string.stamp_3_name),
            context.getString(R.string.stamp_3_description),
            false
        ),
        StampItem(
            "RUN004",
            R.drawable.ic_stamp_4_durability,
            context.getString(R.string.stamp_6_name),
            context.getString(R.string.stamp_6_description),
            false
        ),
        StampItem(
            "RUN005",
            R.drawable.ic_stamp_5_interest,
            context.getString(R.string.stamp_7_name),
            context.getString(R.string.stamp_7_description),
            false
        ),
        StampItem(
            "RUN006",
            R.drawable.ic_stamp_6_growth,
            context.getString(R.string.stamp_8_name),
            context.getString(R.string.stamp_8_description),
            false
        ),
        StampItem(
            "RUN007",
            R.drawable.ic_stamp_7_malice,
            context.getString(R.string.stamp_4_name),
            context.getString(R.string.stamp_4_description),
            false
        ),
        StampItem(
            "RUN008",
            R.drawable.ic_stamp_8_sensitivity,
            context.getString(R.string.stamp_10_name),
            context.getString(R.string.stamp_10_description),
            false
        ),
        StampItem(
            "RUN009",
            R.drawable.ic_stamp_9_delight,
            context.getString(R.string.stamp_9_name),
            context.getString(R.string.stamp_9_description),
            false
        ),
        StampItem(
            "RUN010",
            R.drawable.ic_stamp_10_dash,
            context.getString(R.string.stamp_5_name),
            context.getString(R.string.stamp_5_description),
            false
        ),
    )
}