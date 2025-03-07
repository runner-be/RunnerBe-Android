package com.applemango.presentation.ui.screen.dialog.weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.screen.dialog.CustomBottomSheetDialog
import com.applemango.presentation.util.dpToPx
import com.applemango.presentation.util.recyclerview.LeftSpaceItemDecoration
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogBottomSheetWeatherBinding

class WeatherBottomSheetDialog(
    selectedWeather: WeatherItem,
    currentDegree: String
): CustomBottomSheetDialog<DialogBottomSheetWeatherBinding>(R.layout.dialog_bottom_sheet_weather) {
    private var _weatherAdapter: WeatherAdapter? = null
    private val weatherAdapter get() = _weatherAdapter!!

    private var selectedWeather: WeatherItem
    private var currentTemperature: String

    private var onWeatherConfirmListener: OnWeatherConfirmListener? = null

    private val weatherLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private val smoothScroller: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getHorizontalSnapPreference(): Int = SNAP_TO_START
        }
    }

    init {
        this.selectedWeather = selectedWeather
        this.currentTemperature = if (currentDegree == "-") {
            ""
        } else {
            currentDegree
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStampRecyclerView()
        initClickListeners()
        scrollToSelectedStamp(weatherLayoutManager, selectedWeather)

        binding.tieDegree.setText(currentTemperature.replace("+", ""))
    }

    override fun onDestroyView() {
        _weatherAdapter = null
        super.onDestroyView()
    }

    private fun initClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                if (selectedWeather == WeatherItem.defaultWeatherItem) {
                    onWeatherConfirmListener?.onConfirmClicked(
                        WeatherItem(
                            "WEA001",
                            R.drawable.ic_weather_1_sunny,
                            requireContext().getString(R.string.weather_1_sunny)
                        ),
                        tieDegree.text.toString())
                } else {
                    onWeatherConfirmListener?.onConfirmClicked(selectedWeather, tieDegree.text.toString())
                }

                dismiss()
            }
        }
    }

    private fun initStampRecyclerView() {
        with(binding.rcvWeather) {
            _weatherAdapter = WeatherAdapter()
            adapter = weatherAdapter.apply {
                context?.let {
                    submitList(WeatherItem.initWeatherItems(it))
                }
                setSelectedWeather(selectedWeather)
                setOnWeatherClickListener { weather ->
                    selectedWeather = weather
                }
            }
            layoutManager = weatherLayoutManager
            val space = 24.dpToPx(context)
            addItemDecoration(LeftSpaceItemDecoration(space))
        }
    }

    private fun scrollToSelectedStamp(
        layoutManager: LinearLayoutManager,
        weather: WeatherItem
    ) {
        val weatherList: List<WeatherItem> = WeatherItem.initWeatherItems(requireContext())
        val weatherItem: WeatherItem = WeatherItem.getWeatherItemByCode(requireContext(), weather.code)
        smoothScroller.targetPosition = weatherList.indexOf(weatherItem)
        layoutManager.startSmoothScroll(smoothScroller)
    }

    companion object {
        fun createAndShow(
            fragmentManager: FragmentManager,
            selectedWeather: WeatherItem,
            currentDegree: String,
            onWeatherConfirmListener: OnWeatherConfirmListener
        ) {
            val bottomSheetDialog = WeatherBottomSheetDialog(selectedWeather, currentDegree)
            with(bottomSheetDialog) {
                this.onWeatherConfirmListener = onWeatherConfirmListener
                show(fragmentManager, tag)
            }
        }
    }
}