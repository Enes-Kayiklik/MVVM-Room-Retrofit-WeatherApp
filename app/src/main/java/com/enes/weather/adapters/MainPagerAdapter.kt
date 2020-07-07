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
import com.enes.weather.ui.home.fragments.DisplayFragmentDirections
import com.enes.weather.viewmodel.city.CityViewModel
import com.enes.weather.viewmodel.weather.WeatherViewModel
import kotlinx.android.synthetic.main.city_item_container.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.round

class MainPagerAdapter(
    var cityList: List<City>,
    private val viewModel: CityViewModel,
    private var weatherModel: WeatherViewModel,
    private val owner: LifecycleOwner
) : RecyclerView.Adapter<MainPagerAdapter.CustomViewHolder>() {
    private val calendar = Calendar.getInstance()
    private val months = listOf(
        "Ocak",
        "Şubat",
        "Mart",
        "Nisan",
        "Mayıs",
        "Haziran",
        "Temmuz",
        "Ağustos",
        "Eylül",
        "Ekim",
        "Kasım",
        "Aralık"
    )

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

            weatherModel.weather.observe(owner, Observer {
                it?.let {
                    it.list.first().main.temp.apply {
                        tvWeather.text = round(this).toInt().toString().plus("°")
                    }

                    it.list.first().weather.first().description.apply {
                        tvDescription.text = replace(first(), first().toUpperCase())
                    }

                    val sunrise = Date(it.city.sunrise.toLong() * 1000)
                    val sunset = Date(it.city.sunset.toLong() * 1000)
                    calendar.time = sunrise
                    val sunriseTime =
                        "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
                    calendar.time = sunset
                    val sunsetTime =
                        "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

                    tvDateSunrise.text = sunriseTime
                    tvDateSunset.text = sunsetTime

                    tvDate.text =
                        "${calendar.get(Calendar.DAY_OF_MONTH)} ${months[calendar.get(Calendar.MONTH)]} ${calendar.get(
                            Calendar.YEAR
                        )}"

                    //TODO("GET SELECTED CİTY TİME")
                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    val now = LocalDateTime.now()
                    tvClock.text = formatter.format(now)

                    progressDate.progress = LocalDateTime.now().hour * 100 / 24
                    progressBarDisplayFragment.visibility = View.INVISIBLE
                }
            })
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}