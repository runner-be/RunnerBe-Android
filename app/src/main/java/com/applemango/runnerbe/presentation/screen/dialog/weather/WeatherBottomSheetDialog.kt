package com.applemango.runnerbe.presentation.screen.dialog.weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogBottomSheetStampBinding
import com.applemango.runnerbe.databinding.DialogBottomSheetWeatherBinding
import com.applemango.runnerbe.presentation.screen.dialog.CustomBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.stamp.OnStampConfirmListener
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.LeftSpaceItemDecoration

class WeatherBottomSheetDialog(
    selectedWeather: WeatherItem,
    currentDegree: String
): CustomBottomSheetDialog<DialogBottomSheetWeatherBinding>(R.layout.dialog_bottom_sheet_weather) {
    private var _weatherAdapter: WeatherAdapter? = null
    private val weatherAdapter get() = _weatherAdapter!!

    private var selectedWeather: WeatherItem
    private var currentTemperature: String

    private var onWeatherConfirmListener: OnWeatherConfirmListener? = null

    init {
        this.selectedWeather = selectedWeather
        this.currentTemperature = currentDegree
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStampRecyclerView()
        initClickListeners()

        binding.etTemperature.setText(currentTemperature)
    }

    override fun onDestroyView() {
        _weatherAdapter = null
        super.onDestroyView()
    }

    private fun initClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                onWeatherConfirmListener?.onConfirmClicked(selectedWeather, etTemperature.text.toString())
                dismiss()
            }
        }
    }

    private fun initStampRecyclerView() {
        with(binding.rcvWeather) {
            _weatherAdapter = WeatherAdapter()
            adapter = weatherAdapter.apply {
                submitList(initWeatherItems())
                setSelectedWeather(selectedWeather)
                setOnWeatherClickListener { weather ->
                    selectedWeather = weather
                }
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val space = 24.dpToPx(context)
            addItemDecoration(LeftSpaceItemDecoration(space))
        }
    }

    companion object {
        fun createAndShow(
            fragmentManager: FragmentManager,
            selectedStamp: WeatherItem,
            currentDegree: String,
            onWeatherConfirmListener: OnWeatherConfirmListener
        ) {
            val bottomSheetDialog = WeatherBottomSheetDialog(selectedStamp, currentDegree)
            with(bottomSheetDialog) {
                this.onWeatherConfirmListener = onWeatherConfirmListener
                show(fragmentManager, tag)
            }
        }
    }
}