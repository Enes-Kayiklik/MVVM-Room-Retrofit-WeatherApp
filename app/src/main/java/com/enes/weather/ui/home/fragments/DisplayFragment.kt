@file:Suppress("DEPRECATION")

package com.enes.weather.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.enes.weather.R
import com.enes.weather.adapters.MainPagerAdapter
import com.enes.weather.db.CityDatabase
import com.enes.weather.viewmodel.city.CityViewModel
import com.enes.weather.viewmodel.city.CityViewModelFactory
import com.enes.weather.viewmodel.weather.WeatherViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_display.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class DisplayFragment : Fragment(R.layout.fragment_display) {

    private lateinit var dbViewModel: CityViewModel
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        btnAdd.setOnClickListener {
            navigateAddFragment()
        }
    }

    private fun setupViewPager() {
        pagerAdapter =
            MainPagerAdapter(listOf(), dbViewModel, weatherViewModel, this.viewLifecycleOwner)
        mainPagerContainer.adapter = pagerAdapter

        TabLayoutMediator(tabDots, mainPagerContainer) { tab, _ ->
            mainPagerContainer.setCurrentItem(tab.position, true)
        }.attach()

        dbViewModel.getAllItems().observe(this.viewLifecycleOwner, Observer {
            pagerAdapter.cityList = it
            pagerAdapter.notifyDataSetChanged()
        })

        mainPagerContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                retrofitQuery(position)
            }
        })
    }

    private fun retrofitQuery(position: Int) {
        if (pagerAdapter.cityList.isNotEmpty())
            CoroutineScope(Dispatchers.IO).launch {
                weatherViewModel.getWeatherProperty(pagerAdapter.cityList[position].name)
            }
    }

    private fun setupViewModel() {
        val db = CityDatabase.getInstance(this.requireContext())
        val factory = CityViewModelFactory(db)
        dbViewModel = ViewModelProviders.of(this, factory).get(CityViewModel::class.java)
    }

    private fun navigateAddFragment() {
        findNavController().navigate(R.id.action_displayFragment_to_addFragment)
    }

    override fun onStart() {
        super.onStart()
        setupViewModel()
        setupViewPager()
    }
}