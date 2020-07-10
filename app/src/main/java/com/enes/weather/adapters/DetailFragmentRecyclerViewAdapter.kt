package com.enes.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enes.weather.R
import com.enes.weather.model.WeatherModel
import kotlinx.android.synthetic.main.one_row_detail_weather.view.*
import kotlin.math.round

class DetailFragmentRecyclerViewAdapter(
    var weatherModel: WeatherModel?
) : RecyclerView.Adapter<DetailFragmentRecyclerViewAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.one_row_detail_weather, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.apply {
            weatherModel?.let {
                val tempMin =
                    round(weatherModel?.findMin(position * 7, position * 7 + 7)!!).toInt()
                        .toString()
                        .plus("° / ")
                val tempMax =
                    round(weatherModel?.findMax(position * 7, position * 7 + 7)!!).toInt()
                        .toString()
                        .plus("°")
                tvDetailDegree.text = tempMin.plus(tempMax)


                /*tvDetailDayName.text = when (position) {
                    0 -> "Bugün"
                    1 -> "Yarın"
                    else -> {
                        val date = Date(weatherList[position].dt.toLong() * 1000)
                        calendar.time = date
                        "${calendar.get(Calendar.DAY_OF_MONTH)} ${months[calendar.get(Calendar.MONTH)]} ${calendar.get(
                            Calendar.YEAR
                        )}"
                    }
                }*/

                imgWeatherIcon.setImageDrawable(
                    when (weatherModel?.list?.get(position * 8)!!.weather[0].icon) {
                        "01d", "01n" -> resources.getDrawable(R.drawable.ic_clear_sky, null)
                        "02d", "02n" -> resources.getDrawable(R.drawable.ic_few_clouds, null)
                        "03d", "03n" -> resources.getDrawable(R.drawable.ic_scatterd_clouds, null)
                        "04d", "04n" -> resources.getDrawable(R.drawable.ic_broken_clouds, null)
                        "09d", "09n" -> resources.getDrawable(R.drawable.ic_rain, null)
                        "10d", "10n" -> resources.getDrawable(R.drawable.ic_rain, null)
                        "11d", "11n" -> resources.getDrawable(R.drawable.ic_thunderstorm, null)
                        "13d", "13n" -> resources.getDrawable(R.drawable.ic_snow, null)
                        "50d", "50n" -> resources.getDrawable(R.drawable.ic_mist, null)
                        else -> resources.getDrawable(R.drawable.ic_humidity, null)
                    }
                )
            }
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun WeatherModel.findMin(startIndex: Int, endIndex: Int) =
        this.list.subList(startIndex, endIndex).minBy { it.main.temp_min }?.main?.temp_min

    private fun WeatherModel.findMax(startIndex: Int, endIndex: Int) =
        this.list.subList(startIndex, endIndex).maxBy { it.main.temp_max }?.main?.temp_max
}