package com.example.googlemap

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.googlemap.databinding.FragmentFinallyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException
import java.time.ZoneId
import java.time.ZonedDateTime


class FinallyFragment : Fragment() {

    private lateinit var binding: FragmentFinallyBinding

    val args: FinallyFragmentArgs by navArgs()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFinallyBinding.inflate(layoutInflater, container, false)

        val latitude = args.positionArg[0].toDouble()
        val longitude = args.positionArg[1].toDouble()

        val hour =
            time().hour                                                     //hour,background,text color
        if (0 <= hour && hour <= 16)
            binding.root.setBackgroundResource(R.drawable.day)

        if (16 < hour && hour <= 23) {
            binding.root.setBackgroundResource(R.drawable.night)
            binding.sunday.setTextColor(Color.WHITE)
            binding.monday.setTextColor(Color.WHITE)
            binding.tuesday.setTextColor(Color.WHITE)
            binding.thursday.setTextColor(Color.WHITE)
            binding.friday.setTextColor(Color.WHITE)
            binding.saturday.setTextColor(Color.WHITE)
            binding.wednesday.setTextColor(Color.WHITE)
            binding.temp.setTextColor(Color.WHITE)
            binding.text.setTextColor(Color.WHITE)
            binding.country.setTextColor(Color.WHITE)
            binding.state.setTextColor(Color.WHITE)
            binding.county.setTextColor(Color.WHITE)
            binding.city.setTextColor(Color.WHITE)
            binding.village.setTextColor(Color.WHITE)
            binding.wind.setTextColor(Color.WHITE)
            binding.textTemp.setTextColor(Color.WHITE)
            binding.textWind.setTextColor(Color.WHITE)
            binding.country.backgroundTintList = requireContext().getColorStateList(R.color.blue_1)
            binding.state.backgroundTintList = requireContext().getColorStateList(R.color.blue_2)
            binding.county.backgroundTintList = requireContext().getColorStateList(R.color.blue_3)
            binding.city.backgroundTintList = requireContext().getColorStateList(R.color.blue_4)
            binding.village.backgroundTintList = requireContext().getColorStateList(R.color.blue_5)

        }

        val dayOfWeek = time().dayOfWeek.toString()                                       //week day

        when (dayOfWeek) {
            "SUNDAY" -> binding.vSun.alpha = 1f
            "SATURDAY" -> binding.vSat.alpha = 1f
            "MONDAY" -> binding.vMon.alpha = 1f
            "THURSDAY" -> binding.vThu.alpha = 1f
            "TUESDAY" -> binding.vTue.alpha = 1f
            "FRIDAY" -> binding.vFri.alpha = 1f
            "WEDNESDAY" -> binding.vWed.alpha = 1f
        }


        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible=true
            val job1 = lifecycleScope.async {
                //weather

                var iconCode: String? = null
                val weatherResponse =
                    RetrofitInstance.api.getWeather(latitude = latitude, longitude = longitude)

                if (weatherResponse.isSuccessful && weatherResponse.body() != null) {
                    val weatherData = weatherResponse.body()
                    val text = weatherData!!.main.temp.toInt().toString()
                    binding.temp.setText("$text" + "°C")
                    binding.text.text = weatherData!!.weather[0].description
                    val wind = weatherData.wind.speed.toString()
                    binding.wind.setText("$wind")

                    iconCode = weatherData!!.weather[0].icon
                    Log.e("Log*", "$iconCode")

                } else {
                    Log.e("Weather log***", "response not successful")
                }


                when (iconCode) {                                                        //icon weather
                    "01d" -> binding.img.setImageResource(R.drawable.ic_01d)
                    "01n" -> binding.img.setImageResource(R.drawable.ic_01n)
                    "02d" -> binding.img.setImageResource(R.drawable.ic_02d)
                    "02n" -> binding.img.setImageResource(R.drawable.ic_02n)
                    "03d" -> binding.img.setImageResource(R.drawable.ic_03)
                    "04d" -> binding.img.setImageResource(R.drawable.ic_04)
                    "09" -> binding.img.setImageResource(R.drawable.ic_09)
                    "10d" -> binding.img.setImageResource(R.drawable.ic_10d)
                    "10n" -> binding.img.setImageResource(R.drawable.ic_10n)
                    "11" -> binding.img.setImageResource(R.drawable.ic_11)
                    "13" -> binding.img.setImageResource(R.drawable.ic_13)
                    "50" -> binding.img.setImageResource(R.drawable.ic_50)
                }
                true
            }
            val job2 = lifecycleScope.async(Dispatchers.Main) {
                //location
                val addressResponse =
                    RetrofitInstance.api.gatLocation(latitude = latitude, longitude = longitude)

                if (addressResponse.isSuccessful && addressResponse.body() != null) {

                    val addressData = addressResponse.body()
                    binding.country.text = addressData!!.address.country
                    binding.state.text = addressData.address.state


                    if (addressData.address.county != null) {
                        binding.county.isVisible = true
                        binding.county.text = addressData.address.county
                    }

                    if (addressData.address.city != null) {
                        binding.city.isVisible = true
                        binding.city.text = addressData.address.city
                    }

                    if (addressData.address.village != null) {
                        binding.village.isVisible = true
                        binding.village.text = addressData.address.village
                    }
                } else {

                    Log.e("Address log***", "response not successful")
                }

                true
            }
            if (job1.await() && job2.await()) {

                binding.progressBar.isVisible=false
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun time(): ZonedDateTime {
        val zoneId = ZoneId.of("Asia/Tehran")
        val currentDateTime = ZonedDateTime.now(zoneId)

        return currentDateTime
    }


}