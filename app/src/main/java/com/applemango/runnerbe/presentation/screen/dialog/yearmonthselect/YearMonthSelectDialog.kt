package com.applemango.runnerbe.presentation.screen.dialog.yearmonthselect

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogBottomSheetYearMonthBinding
import com.applemango.runnerbe.presentation.screen.dialog.CustomBottomSheetDialog
import com.applemango.runnerbe.util.NumberUtil
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView
import java.time.LocalDate
import java.util.Calendar

class YearMonthSelectDialog ()
    : CustomBottomSheetDialog<DialogBottomSheetYearMonthBinding>(R.layout.dialog_bottom_sheet_year_month) {
    private var thisYear: Int = LocalDate.now().year
    private var thisMonth: Int = LocalDate.now().monthValue
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
            selectedMonth = binding.wvMonth.getCurrentItem()
            confirmListener(selectedYear.toInt(), selectedMonth.replace("월","").toInt())
            dismiss()
        }
        initWheelViews()
    }

    override fun onDestroyView() {
        dismiss()
        super.onDestroyView()
    }

    private fun initWheelViews() {
        with(binding) {
            val calendar = Calendar.getInstance()
            val (currYear, currMonth) = calendar.run {
                Pair(get(Calendar.YEAR), get(Calendar.MONTH) + 1)
            }
            val yearList = NumberUtil.getRange(currYear, currYear + 10)
            val monthList = getMonthList(currMonth)

            wvYear.apply {
                setData(yearList, yearList.indexOf(yearMonthSelectData.year))
                setOnWheelChangedListener(object : OnWheelChangedListener {
                    override fun onWheelScrolled(view: WheelView?, offset: Int) {
                    }

                    override fun onWheelSelected(view: WheelView?, position: Int) {
                        val selectedYear = yearList[position].toInt()
                         val updatedMonthList = if (selectedYear == thisYear) {
                            getMonthList(thisMonth)
                        } else {
                            getMonthList(12)
                        }

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

    private fun getMonthList(currMonth: Int): List<String> {
        return (1..currMonth).map { month ->
            "${month}월"
        }
    }

    companion object {
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