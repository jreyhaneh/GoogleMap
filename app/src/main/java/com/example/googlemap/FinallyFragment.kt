package com.example.googlemap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.googlemap.databinding.FragmentFinallyBinding
import retrofit2.HttpException
import java.io.IOException


class FinallyFragment : Fragment() {

    private lateinit var binding: FragmentFinallyBinding

    val args: FinallyFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFinallyBinding.inflate(layoutInflater, container, false)

        val latitude = args.positionArg[0].toDouble()
        val longitude = args.positionArg[1].toDouble()


        lifecycleScope.launchWhenCreated {
            val weatherResponse = try {
                RetrofitInstance.api.getWeather(latitude = latitude, longitude = longitude)

            } catch (e: IOException) {
                Log.e(
                    "Weather log*",
                    "IOexeption, you might not have internet connection: ${e.message}"
                )
                return@launchWhenCreated

            } catch (e: HttpException) {
                Log.e("Weather log**", "HttpEXception , unexpected response")
                return@launchWhenCreated

            }
            if (weatherResponse.isSuccessful && weatherResponse.body() != null) {
                val weatherData = weatherResponse.body()
               binding.text.text= weatherData!!.weather[0].description
                binding.temp.text=weatherData!!.main.temp.toString()


            } else {
                Log.e("Weather log***", "response not successful")
            }

        }

        lifecycleScope.launchWhenCreated {
            val addressResponse = try {
                RetrofitInstance.api.gatLocation(latitude = latitude, longitude = longitude)

            } catch (e: IOException) {
                Log.e("Address log*", "IOexeption, you might not have internet connection")
                return@launchWhenCreated

            } catch (e: HttpException) {
                Log.e("Address log**", "HttpEXception , unexpected response")
                return@launchWhenCreated
            }
            if (addressResponse.isSuccessful && addressResponse.body() != null) {

                val addressData=addressResponse.body()
                binding.country.text=addressData!!.address.country
                binding.county.text=addressData!!.address.county
                binding.city.text=addressData!!.address.city
                binding.state.text=addressData!!.address.state
                binding.village.text=addressData!!.address.village

            } else {

                Log.e("Address log***", "response not successful")
            }

        }

        return binding.root
    }


}