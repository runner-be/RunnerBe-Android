package com.applemango.runnerbe.presentation.screen.dialog.weather

import android.content.Context
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication

data class WeatherItem(
    val code: String,
    val image: Int,
    val name: String,
) {
    companion object {
        val defaultWeatherItem = WeatherItem(
            "WEA000",
            R.drawable.ic_weather_default,
            "기본"
        )

        fun getWeatherItemByCode(context: Context, code: String?) : WeatherItem {
            val weatherList = initWeatherItems(context)
            return weatherList.firstOrNull {
                it.code == code
            } ?: WeatherItem(
                "WEA001",
                R.drawable.ic_weather_1_sunny,
                context.getString(R.string.weather_1_sunny)
            )
        }

        fun initWeatherItems(context: Context): List<WeatherItem> {
            return listOf(
                WeatherItem(
                    "WEA001",
                    R.drawable.ic_weather_1_sunny,
                    context.getString(R.string.weather_1_sunny)
                ),
                WeatherItem(
                    "WEA002",
                    R.drawable.ic_weather_2_cloudy,
                    context.getString(R.string.weather_2_sunny)
                ),
                WeatherItem(
                    "WEA003",
                    R.drawable.ic_weather_3_night,
                    context.getString(R.string.weather_3_sunny)
                ),
                WeatherItem(
                    "WEA004",
                    R.drawable.ic_weather_4_rainy,
                    context.getString(R.string.weather_4_sunny)
                ),
                WeatherItem(
                    "WEA005",
                    R.drawable.ic_weather_5_snowy,
                    context.getString(R.string.weather_5_sunny)
                ),
            )
        }
    }
}