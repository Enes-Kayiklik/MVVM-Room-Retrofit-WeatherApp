package com.enes.weather.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.enes.weather.R
import com.enes.weather.db.City
import com.enes.weather.model.WeatherModel
import com.enes.weather.ui.home.fragments.DisplayFragmentDirections
import com.enes.weather.viewmodel.city.CityViewModel
import com.enes.weather.viewmodel.weather.WeatherViewModel
import kotlinx.android.synthetic.main.city_item_container.view.*
import kotlin.math.round

class MainPagerAdapter(
    var cityList: List<City>,
    private val viewModel: CityViewModel,
    private var weatherModel: WeatherViewModel,
    private val owner: LifecycleOwner
) : RecyclerView.Adapter<MainPagerAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.city_item_container, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.apply {
            tvCityName.text = cityList[position].name

            btnDelete.setOnClickListener {
                viewModel.delete(cityList[position])
            }

            btnMoreInformation.setOnClickListener {
                val action = DisplayFragmentDirections.actionDisplayFragmentToDetailFragment(
                    cityName = cityList[position].name
                )
                findNavController().navigate(action)
            }

            weatherModel.weather.observe(owner, Observer { observe ->
                observe?.let { weatherModel ->
                    weatherModel.list.first().main.temp.apply {
                        tvWeather.text = round(this).toInt().toString()
                    }

                    imgWeatherIcon.setImageDrawable(
                        when (weatherModel.list[0].weather[0].icon) {
                            "01d", "01n" -> resources.getDrawable(R.drawable.ic_clear_sky, null)
                            "02d", "02n" -> resources.getDrawable(R.drawable.ic_few_clouds, null)
                            "03d", "03n" -> resources.getDrawable(
                                R.drawable.ic_scatterd_clouds,
                                null
                            )
                            "04d", "04n" -> resources.getDrawable(R.drawable.ic_broken_clouds, null)
                            "09d", "09n" -> resources.getDrawable(R.drawable.ic_rain, null)
                            "10d", "10n" -> resources.getDrawable(R.drawable.ic_rain, null)
                            "11d", "11n" -> resources.getDrawable(R.drawable.ic_thunderstorm, null)
                            "13d", "13n" -> resources.getDrawable(R.drawable.ic_snow, null)
                            "50d", "50n" -> resources.getDrawable(R.drawable.ic_mist, null)
                            else -> resources.getDrawable(R.drawable.ic_humidity, null)
                        }
                    )
                    val tempMin = round(weatherModel.findMin()!!).toInt().toString().plus("° / ")
                    val tempMax = round(weatherModel.findMax()!!).toInt().toString().plus("°")
                    tvMinMaxDegree.text = tempMin.plus(tempMax)

                    val currentDec = weatherModel.list[0].main
                    tvWind.text = weatherModel.list[0].wind.speed.toString().plus(" km/h")
                    tvHumidity.text = currentDec.humidity.toString()
                    tvPressure.text = currentDec.pressure.toString().plus(" hPa")
                    tvRealFeel.text = currentDec.feels_like.toString().plus(" °")

                    weatherModel.list.first().weather.first().description.apply {
                        tvDescription.text = replace(first(), first().toUpperCase())
                    }

                    /*val sunrise = Date(it.city.sunrise.toLong() * 1000)
                    val sunset = Date(it.city.sunset.toLong() * 1000)
                    calendar.time = sunrise
                    val sunriseTime =
                        "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
                    calendar.time = sunset
                    val sunsetTime =
                        "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

                    tvDateSunrise.text = sunriseTime
                    tvDateSunset.text = sunsetTime*/

                    progressBarDisplayFragment.visibility = View.INVISIBLE
                }
            })
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun WeatherModel.findMin() =
        this.list.subList(0, 8).minBy { it.main.temp_min }?.main?.temp_min

    private fun WeatherModel.findMax() =
        this.list.subList(0, 8).maxBy { it.main.temp_min }?.main?.temp_max
}