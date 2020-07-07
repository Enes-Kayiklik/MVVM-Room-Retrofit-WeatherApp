package com.enes.weather.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enes.weather.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        /*val viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        viewModel.weather.observe(this, Observer {
            Log.e("Deneme", "$it")
        })*/

    }
}