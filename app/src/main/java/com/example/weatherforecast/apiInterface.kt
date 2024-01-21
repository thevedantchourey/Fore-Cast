package com.example.weatherforecast

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface apiInterface {
    @GET("weather")
    fun getWeatherData(@Query("q")city:String,@Query("appId")appId:String,@Query("units")units:String): Call<weatherforecast>
}