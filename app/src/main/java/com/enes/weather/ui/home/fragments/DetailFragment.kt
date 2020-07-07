package com.enes.weather.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.enes.weather.R
import com.enes.weather.adapters.DetailFragmentRecyclerViewAdapter
import com.enes.weather.viewmodel.weather.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var detailAdapter: DetailFragmentRecyclerViewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        weatherViewModel.weather.observe(this.viewLifecycleOwner, Observer { weatherInfo ->
            val informationList = listOf(
                weatherInfo.list[0],
                weatherInfo.list[8],
                weatherInfo.list[16],
                weatherInfo.list[24],
                weatherInfo.list[32]
            )

            detailAdapter.weatherList = informationList
            detailAdapter.notifyDataSetChanged()

            val weatherMain = weatherInfo.list[0].main
            tvDetailHumidity.text = "${weatherMain.humidity}"
            tvDetailPressure.text = "${weatherMain.pressure} hPa"
            tvDetailWind.text = "${weatherInfo.list[0].wind.speed} km/h"
            progressBarDetailFragment.visibility = View.INVISIBLE
        })
    }

    private fun getCityData(cityName: String) {
        progressBarDetailFragment.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            weatherViewModel.getWeatherProperty(cityName)
        }
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        val name = DetailFragmentArgs.fromBundle(this.requireArguments()).cityName
        getCityData(name!!)
    }

    private fun setupRecyclerView() {
        detailAdapter = DetailFragmentRecyclerViewAdapter(listOf())
        recyclerViewDetailContainer.adapter = detailAdapter
        recyclerViewDetailContainer.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}