package com.applemango.presentation.ui.screen.dialog.yearmonthselect

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.applemango.presentation.ui.screen.dialog.CustomBottomSheetDialog
import com.applemango.presentation.util.NumberUtil
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogBottomSheetYearMonthBinding
import com.applemango.presentation.util.LogUtil
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView
import java.time.LocalDate
import java.util.Calendar

class YearMonthSelectDialog ()
    : CustomBottomSheetDialog<DialogBottomSheetYearMonthBinding>(R.layout.dialog_bottom_sheet_year_month) {
    private val thisYear: Int = LocalDate.now().year
    private val thisMonth: Int = LocalDate.now().monthValue
    private var selectedYear: String = ""
    private var selectedMonth: String = ""
    private lateinit var yearMonthSelectData: YearMonthSelectData
    private lateinit var confirmListener: ((Int, Int) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedYear = yearMonthSelectData.year
        selectedMonth = yearMonthSelectData.month
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            selectedYear = binding.wvYear.getCurrentItem()
            selectedMonth = binding.wvMonth.getCurrentItem<String>().replace("월","")

            confirmListener(selectedYear.toInt(), selectedMonth.toInt())
            dismiss()
        }
        initWheelViews()
    }

    override fun onPause() {
        LogUtil.debugLog("onPause")
        super.onPause()
    }

    override fun onStop() {
        LogUtil.debugLog("onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        LogUtil.debugLog("onDestroyView")
        dismiss()
        super.onDestroyView()
    }

    private fun initWheelViews() {
        with(binding) {
            val (currYear, currMonth) = Pair(selectedYear.toInt(), selectedMonth.toInt())
            val yearList = NumberUtil.getRange(MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR))
            val monthList = getMonthList(currYear, selectedMonth.toInt())

            wvYear.apply {
                setData(yearList, yearList.indexOf(yearMonthSelectData.year))
                setOnWheelChangedListener(object : OnWheelChangedListener {
                    override fun onWheelScrolled(view: WheelView?, offset: Int) {
                    }

                    override fun onWheelSelected(view: WheelView?, position: Int) {
                        val selectedYear = yearList[position].toInt()
                        val updatedMonthList = getMonthList(selectedYear, currMonth)

                        wvMonth.setData(updatedMonthList, updatedMonthList.indexOf("${yearMonthSelectData.month}월"))
                    }

                    override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
                    }

                    override fun onWheelLoopFinished(view: WheelView?) {
                    }
                })
            }

            // 초기 monthList 설정
            wvMonth.setData(monthList, monthList.indexOf("${yearMonthSelectData.month}월"))
        }
    }

    private fun getMonthList(currYear: Int, currMonth: Int): List<String> {
        return if (currYear == thisYear) {
            (1..thisMonth).map { month ->
                "${month}월"
            }
        } else {
            (1..12).map { month ->
                "${month}월"
            }
        }
    }

    companion object {
        const val MIN_YEAR = 2024
        const val MIN_MONTH = 1

        fun createAndShow(
            fragmentManager: FragmentManager,
            yearMonthSelectData: YearMonthSelectData,
            confirmListener: (Int, Int) -> Unit
        ) {
            val bottomSheetDialog = YearMonthSelectDialog()
            with(bottomSheetDialog) {
                this.yearMonthSelectData = yearMonthSelectData
                this.confirmListener = confirmListener
                show(fragmentManager, tag)
            }
        }
    }
}