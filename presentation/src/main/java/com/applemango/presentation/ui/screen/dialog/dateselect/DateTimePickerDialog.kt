package com.applemango.presentation.ui.screen.dialog.dateselect

import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.applemango.presentation.ui.model.listener.DateResultListener
import com.applemango.presentation.util.NumberUtil
import com.applemango.presentation.util.TimeUtil
import com.applemango.presentation.util.ToastUtil
import com.applemango.presentation.util.getDateList
import com.applemango.presentation.util.getYearListByDay
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogDateSelectBinding
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class DateTimePickerDialog(context: Context) : Dialog(context, R.style.confirmDialogStyle) {

    val binding: DialogDateSelectBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_date_select,
            null,
            false
        )
    }

    var isAm = true
    lateinit var dateResultListener: DateResultListener
    var currentDate: DateSelectData? = null
    private val yearList = getYearListByDay(DEFAULT_DATE_SIZE)

    companion object {

        private const val DEFAULT_DATE_SIZE = 21
        fun createShow(
            context: Context,
            isAm: Boolean = true,
            displayDate: DateSelectData? = null,
            resultListener: DateResultListener
        ) {
            val dialog = DateTimePickerDialog(context)
            with(dialog) {
                this.isAm = isAm
                this.currentDate = displayDate
                this.isAm = displayDate?.AMAndPM == "AM"
                this.dateResultListener = resultListener
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initWheelViewData()
        initListener()
        initAmPmChangeListener()
    }

    private fun initListener() {
        binding.tvConfirm.setOnClickListener {
            if (getIsSelectedItemInvalid(binding.wvDate.getCurrentItem())) {
                ToastUtil.showShortToast(context, "현재 시각 이후만 선택이 가능해요")
                return@setOnClickListener
            }

            changeDisplayToDate()?.let { completeDate ->
                dateResultListener.getDate(
                    completeDate, DateSelectData(
                        formatDate = getDate(),
                        AMAndPM = getAm(),
                        hour = getHour(),
                        minute = getMinute()
                    )
                )
            }

            dismiss()
        }
    }

    private fun initAmPmChangeListener() {
        binding.wvAmPm.setOnWheelChangedListener(object: OnWheelChangedListener {
            override fun onWheelScrolled(view: WheelView?, offset: Int) {
            }

            override fun onWheelSelected(view: WheelView?, position: Int) {
                if (position == 0) {
                    binding.wvHour.data = NumberUtil.getRange(0, 11)
                } else {
                    binding.wvHour.data = listOf("12") + NumberUtil.getRange(1, 11)
                }
            }

            override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
            }

            override fun onWheelLoopFinished(view: WheelView?) {
            }

        })
    }

    private fun initWheelViewData() {
        with(binding) {
            val dateList = getDateList(DEFAULT_DATE_SIZE)
            val ampmList = listOf("AM", "PM")
            val hourList: List<String> = if (isAm) {
                NumberUtil.getRange(0, 11)
            } else {
                listOf("12") + NumberUtil.getRange(1, 11)
            }
            val minuteList = NumberUtil.getUnitNumber(0, 55, 5)

            wvDate.setData(
                dateList,
                dateList.indexOf(currentDate?.formatDate)
            )
            wvAmPm.setData(ampmList, if (isAm) 0 else 1)
            wvHour.setData(hourList, hourList.indexOf(currentDate?.hour))
            wvMinute.setData(minuteList, minuteList.indexOf(currentDate?.minute))
        }
    }

    private fun getIsToday(dateString: String): Boolean {
        val inputDate = dateString.split(" ")[0] // "11/3 (월)" -> "11/3"
        val splittedDate = inputDate.split("/")
        val (month, date) = Pair(splittedDate[0].toInt(), splittedDate[1].toInt())
        val today = LocalDate.now()

        return (month == today.monthValue) && (date == today.dayOfMonth)
    }

    private fun getIsSelectedItemInvalid(selectedItem: String): Boolean {
        if (!getIsToday(selectedItem)) return false

        val currentTotalMinutes: Int = Calendar.getInstance().run {
            get(Calendar.HOUR_OF_DAY) * 60 + get(Calendar.MINUTE)
        }

        val selectedTotalMinutes: Int = with(binding) {
            val selectedAmPm = binding.wvAmPm.getCurrentItem<String>()
            val selectedHour = binding.wvHour.getCurrentItem<String>().toInt().let { hour ->
                when {
                    selectedAmPm == "PM" && hour == 12 -> 12
                    selectedAmPm == "AM" && hour == 12 -> 0
                    selectedAmPm == "PM" -> hour + 12
                    else -> hour
                }
            }
            val selectedMinute = binding.wvMinute.getCurrentItem<String>().toInt()
            selectedHour * 60 + selectedMinute
        }

        return currentTotalMinutes >= selectedTotalMinutes
    }


    private fun changeDisplayToDate(): Date? {
        val dateSplit = getDate().split("/")
        val hour = TimeUtil.getAMAndPMTo24(
            binding.wvAmPm.getCurrentItem<String>() == "AM",
            binding.wvHour.getCurrentItem<String>().toInt()
        )
        val date = "${yearList[binding.wvDate.currentPosition]}-${dateSplit[0]}-${dateSplit[1].split(" ")[0]
        } $hour:${binding.wvMinute.getCurrentItem<String>()}:00"
        return SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(date)
    }

    private fun getDate(): String = binding.wvDate.getCurrentItem()
    private fun getAm(): String = binding.wvAmPm.getCurrentItem()
    private fun getHour(): String = binding.wvHour.getCurrentItem()
    private fun getMinute(): String = binding.wvMinute.getCurrentItem()
}