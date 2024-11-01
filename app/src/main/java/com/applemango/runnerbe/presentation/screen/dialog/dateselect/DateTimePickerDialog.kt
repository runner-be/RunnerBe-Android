package com.applemango.runnerbe.presentation.screen.dialog.dateselect

import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogDateSelectBinding
import com.applemango.runnerbe.presentation.model.DateResultListener
import com.applemango.runnerbe.util.NumberUtil
import com.applemango.runnerbe.util.TimeUtil
import com.applemango.runnerbe.util.getDateList
import com.applemango.runnerbe.util.getYearListByDay
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
    var currentDate: DateSelectData? = null
    lateinit var result: DateResultListener
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
                this.result = resultListener
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initWheelViewData()
        initListener()
    }

    private fun initListener() {
        binding.tvConfirm.setOnClickListener {
            changeDisplayToDate()?.let { completeDate ->
                result.getDate(
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

    private fun initWheelViewData() {
        with(binding) {
            val dateList = getDateList(DEFAULT_DATE_SIZE)
            val hourList = getTodayHourList()
            val minuteList = NumberUtil.getUnitNumber(0, 55, 5)
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR)

            wvDate.apply {
                setData(
                    dateList,
                    dateList.indexOf(currentDate?.formatDate)
                )
                setOnWheelChangedListener(object : OnWheelChangedListener {
                    override fun onWheelScrolled(view: WheelView?, offset: Int) {
                    }

                    override fun onWheelSelected(view: WheelView?, position: Int) {
                        val selectedDate = dateList[position]
                        val calendar = Calendar.getInstance()
                        val currentAmPm = calendar.get(Calendar.AM_PM)
                        val currentHour = calendar.get(Calendar.HOUR)
                        val isToday = getIsToday(selectedDate)

                        val updatedAmPmList = if (isToday && currentAmPm == Calendar.PM) {
                            listOf("PM")
                        } else {
                            listOf("AM", "PM")
                        }
                        val updatedHourList = if (isToday) {
                            NumberUtil.getRange(currentHour + 1, 12)
                        } else {
                            NumberUtil.getRange(0, 12)
                        }
                        wvAmPm.setData(updatedAmPmList, updatedAmPmList.indexOf(currentAmPm.toString()))
                        wvHour.setData(updatedHourList, 0)
                    }

                    override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
                    }

                    override fun onWheelLoopFinished(view: WheelView?) {
                    }

                })
            }
            wvAmPm.setData(getTodayAmPmList(), if (isAm) 0 else 1)
            wvHour.setData(hourList, 0)
            wvMinute.setData(minuteList, minuteList.indexOf(currentDate?.minute))
        }
    }

    private fun getTodayHourList(): List<String> {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR)
        return NumberUtil.getRange(currentHour + 1, 12)
    }

    private fun getTodayAmPmList(): List<String> {
        val today = LocalDate.now()
        val todayString = "${today.monthValue}/${today.dayOfMonth}"
        val calendar = Calendar.getInstance()
        val currentAmPm = calendar.get(Calendar.AM_PM)
        val isToday = getIsToday(todayString)
        return if (isToday && currentAmPm == Calendar.PM) {
            listOf("PM")
        } else {
            listOf("AM", "PM")
        }
    }

    private fun getIsToday(dateString: String): Boolean {
        val inputDate = dateString.split(" ")[0] // "11/3 (월)" -> "11/3"
        val splittedDate = inputDate.split("/")
        val (month, date) = Pair(splittedDate[0].toInt(), splittedDate[1].toInt())
        val today = LocalDate.now()

        return (month == today.monthValue) && (date == today.dayOfMonth)
    }

    private fun changeDisplayToDate(): Date? {
        val dateSplit = getDate().split("/")
        val hour = TimeUtil.getAMAndPMTo24(
            binding.wvAmPm.getCurrentItem<String>() == "AM",
            binding.wvHour.getCurrentItem<String>().toInt()
        )
        val date = "${yearList[binding.wvDate.currentPosition]}-${dateSplit[0]}-${
            dateSplit[1].split(" ")[0]
        } $hour:${binding.wvMinute.getCurrentItem<String>()}:00"
        return SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(date)
    }

    private fun getDate(): String = binding.wvDate.getCurrentItem()
    private fun getAm(): String = binding.wvAmPm.getCurrentItem()
    private fun getHour(): String = binding.wvHour.getCurrentItem()
    private fun getMinute(): String = binding.wvMinute.getCurrentItem()
}