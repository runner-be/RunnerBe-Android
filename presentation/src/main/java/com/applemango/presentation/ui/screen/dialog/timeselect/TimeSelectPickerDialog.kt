package com.applemango.presentation.ui.screen.dialog.timeselect

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.applemango.presentation.ui.model.listener.TimeResultListener
import com.applemango.presentation.util.NumberUtil
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogTimeSelectBinding
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView

class TimeSelectPickerDialog(context: Context) : Dialog(context, R.style.confirmDialogStyle) {

    private val binding: DialogTimeSelectBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_time_select,
            null,
            false
        )
    }

    lateinit var result: TimeResultListener
    private var timeSelectData: TimeSelectData? = null

    companion object {

        fun createShow(
            context: Context,
            selectedData: TimeSelectData? = null,
            resultListener: TimeResultListener
        ) {
            val dialog = TimeSelectPickerDialog(context)
            with(dialog) {
                this.result = resultListener
                this.timeSelectData = selectedData
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWheelView()
        setClickListeners()
        setContentView(binding.root)
    }

    private fun initWheelView() {
        val hourList = NumberUtil.getRange(0, 5)
        val minuteList = NumberUtil.getUnitNumber(0, 50, 10)
        binding.hourWheelView.setData(hourList, hourList.indexOf(timeSelectData?.hour))
        setMinuteData(minuteList)
        binding.hourWheelView.setOnWheelChangedListener(object : OnWheelChangedListener {
            override fun onWheelScrolled(view: WheelView?, offset: Int) {}

            override fun onWheelSelected(view: WheelView?, position: Int) {
                setMinuteData(minuteList)
            }

            override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {}

            override fun onWheelLoopFinished(view: WheelView?) {}
        })
    }

    private fun setClickListeners() {
        with(binding) {
            confirmButton.setOnClickListener {
                result.getDate(
                    TimeSelectData(
                        hour = binding.hourWheelView.getCurrentItem(),
                        minute = binding.minuteWheelView.getCurrentItem()
                    )
                )
                dismiss()
            }
        }
    }

    private fun setMinuteData(minuteList: List<String>) {
        if (binding.hourWheelView.getCurrentItem<String>() == "5") {
            binding.minuteWheelView.setData(listOf("0"), 0)
        } else {
            binding.minuteWheelView.setData(
                minuteList,
                minuteList.indexOf(timeSelectData?.minute)
            )
        }
    }
}