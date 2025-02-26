package com.applemango.presentation.ui.screen.fragment.mypage.calendar.monthly

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.screen.dialog.stamp.StampItem
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.DateItem
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.OnDateClickListener
import com.applemango.presentation.util.ToastUtil
import com.applemango.presentation.R
import com.applemango.presentation.databinding.ItemDateMonthlyBinding
import com.applemango.presentation.databinding.ItemDateMonthlyEmptyBinding
import java.time.LocalDate

class MonthlyEmptyViewHolder (
    val binding: ItemDateMonthlyEmptyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem) {
        val date = item.date
        with(binding) {
            if (date != null) {
                tvDate.text = date.dayOfMonth.toString()
            } else {
                tvDate.text = ""
            }
        }
    }
}

class MonthlyOtherUserDateViewHolder (
    private val binding: ItemDateMonthlyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, onDateClickListener: OnDateClickListener) {
        with(binding) {
            item.date?.let {
                tvDate.apply {
                    text = it.dayOfMonth.toString()
                    if (it == LocalDate.now()) {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g2, null))
                    } else {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g5, null))
                    }
                }
            }
            item.runningLog?.let { log ->
                // 다른 사람 로그 목록에서는 그룹 로그 작성이 가능해도 변경 불가능 이미지로 표기
                val stampImage = if (log.stampCode == null || log.stampCode == StampItem.availableStampItem.code) {
                    StampItem.otherUserUnavailableStampItem.image
                } else {
                    StampItem.getStampItemByCode(itemView.context, log.stampCode)?.image!!
                }
                ivStamp.setImageResource(stampImage)
                llDate.setOnClickListener {
                    if (item.runningLog.isOpened == 1) {
                        onDateClickListener.onDateClicked(item)
                    } else {
                        ToastUtil.showShortToast(itemView.context, "열람할 수 없는 로그입니다")
                    }
                }
            } ?: ivStamp.setImageResource(StampItem.otherUserUnavailableStampItem.image)
        }
    }
}

class MonthlyDateViewHolder (
    private val binding: ItemDateMonthlyBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DateItem, onDateClickListener: OnDateClickListener) {
        with(binding) {
            item.date?.let {
                tvDate.apply {
                    text = it.dayOfMonth.toString()
                    if (it == LocalDate.now()) {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g2, null))
                    } else {
                        setTextColor(ResourcesCompat.getColor(context.resources, R.color.dark_g5, null))
                    }
                }
            }
            item.runningLog?.let { log ->
                if (log.stampCode == null && log.gatheringId != null) {
                    ivStamp.setImageResource(StampItem.availableStampItem.image)
                } else {
                    ivStamp.setImageResource(StampItem.getStampItemByCode(itemView.context, log.stampCode)!!.image)
                }
            } ?: ivStamp.setImageResource(StampItem.unavailableStampItem.image)
            llDate.setOnClickListener {
                onDateClickListener.onDateClicked(item)
            }
        }
    }
}