package com.example.weatherforecast

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.weatherforecast.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Indore")
        searchCity()
        }

    private fun searchCity(){
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName: String){
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
            "https://api.openweathermap.org/data/2.5/").build().create(apiInterface::class.java)
        val response = retrofit.getWeatherData(cityName,"67a27574e9ff6d5c4b49ae1ce2b02989","metric")
        response.enqueue(object :Callback<weatherforecast> {
            override fun onResponse(
                call: Call<weatherforecast>,
                response: Response<weatherforecast>
            ) {
                val temp = binding.temp
                val maxT = binding.maxTemp
                val minT = binding.minTemp
                val weather = binding.weather
                val day = binding.day
                val date = binding.date
                val city = binding.city
                val humidity = binding.humidityValue
                val sunrise = binding.sunriseValue
                val sunset = binding.sunsetValue
                val condition = binding.conditionValue
                val sea = binding.seaValue
                val wind = binding.windSpeedValue
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature=responseBody.main.temp.toString()
                    val maxTemp =responseBody.main.temp_max.toString()
                    val minTemp =responseBody.main.temp_min.toString()
                    val sunriseValue =responseBody.sys.sunrise
                    val sunsetValue =responseBody.sys.sunset
                    val humidityValue =responseBody.main.humidity.toString()
                    val seaLevel =responseBody.main.pressure.toString()
                    val windSpeed =responseBody.wind.speed.toString()
                    val conditionValue =responseBody.weather.firstOrNull()?.main?:"unknown"
                    temp.text = getString(R.string.tempValue,temperature)
                    maxT.text = getString(R.string.max, maxTemp)
                    minT.text = getString(R.string.min, minTemp)
                    weather.text = getString(R.string.temp,conditionValue)
                    sunrise.text = getString(R.string.title4value,time(sunriseValue.toLong()))
                    sunset.text = getString(R.string.title5value,time(sunsetValue.toLong()))
                    humidity.text = getString(R.string.title1value,"$humidityValue %")
                    condition.text = getString(R.string.title3value,conditionValue)
                    sea.text = getString(R.string.title6value,seaLevel)
                    day.text = dayName()
                    date.text = exactDate()
                    wind.text = getString(R.string.title2value,windSpeed)
                    city.text = cityName
                    changeBackgroundOnCondition(conditionValue)
                }
            }
            override fun onFailure(call: Call<weatherforecast>, t: Throwable) {
            }
        })
    }

    private fun changeBackgroundOnCondition(conditions:String) {
        when(conditions){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundColor(Color.parseColor("#99ccff"))
                binding.lottieAnimationView.setAnimation(R.raw.sun)
                binding.linearLayout.setBackgroundResource(R.drawable.backgroundshape2)
                binding.linearLayout2.setBackgroundResource(R.drawable.backgroundshape2)
                binding.linearLayout3.setBackgroundResource(R.drawable.backgroundshape2)
                binding.linearLayout4.setBackgroundResource(R.drawable.backgroundshape2)
                binding.linearLayout5.setBackgroundResource(R.drawable.backgroundshape2)
                binding.linearLayout6.setBackgroundResource(R.drawable.backgroundshape2)
            }
            "Partly Clouds","Clouds","Overcast","Mist","Foggy","Haze"->{
                binding.root.setBackgroundColor(Color.parseColor("#cccccc"))
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
                binding.linearLayout.setBackgroundResource(R.drawable.backgroundshape3)
                binding.linearLayout2.setBackgroundResource(R.drawable.backgroundshape3)
                binding.linearLayout3.setBackgroundResource(R.drawable.backgroundshape3)
                binding.linearLayout4.setBackgroundResource(R.drawable.backgroundshape3)
                binding.linearLayout5.setBackgroundResource(R.drawable.backgroundshape3)
                binding.linearLayout6.setBackgroundResource(R.drawable.backgroundshape3)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundColor(Color.parseColor("#66b3ff"))
                binding.lottieAnimationView.setAnimation(R.raw.rain)
                binding.linearLayout.setBackgroundResource(R.drawable.backgroundshape4)
                binding.linearLayout2.setBackgroundResource(R.drawable.backgroundshape4)
                binding.linearLayout3.setBackgroundResource(R.drawable.backgroundshape4)
                binding.linearLayout4.setBackgroundResource(R.drawable.backgroundshape4)
                binding.linearLayout5.setBackgroundResource(R.drawable.backgroundshape4)
                binding.linearLayout6.setBackgroundResource(R.drawable.backgroundshape4)
            }
            "Light Snow","Moderate Snow","Blizzard","Heavy Snow"->{
                binding.root.setBackgroundColor(Color.parseColor("#000080"))
                binding.lottieAnimationView.setAnimation(R.raw.snow)
                binding.linearLayout.setBackgroundResource(R.drawable.backgroundshape5)
                binding.linearLayout2.setBackgroundResource(R.drawable.backgroundshape5)
                binding.linearLayout3.setBackgroundResource(R.drawable.backgroundshape5)
                binding.linearLayout4.setBackgroundResource(R.drawable.backgroundshape5)
                binding.linearLayout5.setBackgroundResource(R.drawable.backgroundshape5)
                binding.linearLayout6.setBackgroundResource(R.drawable.backgroundshape5)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun exactDate(): CharSequence? {
        val sdf  = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun dayName(): String {
        val sdf  = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long): String {
        val sdf  = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

}
