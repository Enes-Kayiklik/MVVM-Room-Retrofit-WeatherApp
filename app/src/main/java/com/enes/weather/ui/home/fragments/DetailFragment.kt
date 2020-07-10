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
            detailAdapter.weatherModel = weatherInfo
            detailAdapter.notifyDataSetChanged()

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
        detailAdapter = DetailFragmentRecyclerViewAdapter(null)
        recyclerViewDetailContainer.adapter = detailAdapter
        recyclerViewDetailContainer.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}