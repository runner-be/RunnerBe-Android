package com.applemango.presentation.ui.screen.dialog.weather

fun interface OnWeatherConfirmListener {
    fun onConfirmClicked(weather: WeatherItem, temperature: String)
}