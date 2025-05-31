package com.schuetz.agents

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

interface WeatherClient {
    suspend fun getTodaysWeather(): Result<WeatherClientReport>
}

class DummyWeatherClient : WeatherClient {
    override suspend fun getTodaysWeather(): Result<WeatherClientReport> {
        delay(2000)
        return Result.success(WeatherClientReport(19.123, 47.02))
    }
}

@Serializable
data class WeatherClientReport(val temperature: Double, val humidity: Double)
