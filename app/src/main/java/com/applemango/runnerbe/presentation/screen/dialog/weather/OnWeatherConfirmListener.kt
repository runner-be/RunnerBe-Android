package com.applemango.runnerbe.presentation.screen.dialog.weather

fun interface OnWeatherConfirmListener {
    fun onConfirmClicked(weather: WeatherItem, temperature: String)
}