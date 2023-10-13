package com.example.googlemap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.googlemap.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private var mGoogleMap: GoogleMap? = null
    private var latLng: LatLng? = null
    private var marker: Marker? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)
//
//        val x = OnMapReadyCallback { googleMap ->
//            mGoogleMap = googleMap
//        }


        return binding.root
    }


    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0


        val markerView = layoutInflater.inflate(
            R.layout.market_layout, binding.root as ViewGroup,
            false
        )
        binding.buttonLocation.setOnClickListener {
            val zoom = 12
            val tilt = 0
            val bearing = 0

            fetchLocation()
            if (latLng != null) {
                if (marker != null) {
                    marker!!.remove()
                }
                val camera =
                    CameraPosition(latLng!!, zoom.toFloat(), tilt.toFloat(), bearing.toFloat())
                mGoogleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
                marker = mGoogleMap!!.addMarker(MarkerOptions().position(latLng!!))
            }
        }

//++++
        mGoogleMap!!.setOnMapClickListener { location ->
            if (marker != null)
                marker!!.remove()
            marker =
                mGoogleMap!!.addMarker(MarkerOptions().position(location))
        }
//++++

        binding.buttonSet.setOnClickListener {                             //send args
            val latitude = marker!!.position.latitude.toFloat()
            val longitude = marker!!.position.longitude.toFloat()
            val position = floatArrayOf(latitude, longitude)

            val action = MapFragmentDirections.actionMapFragmentToFinallyFragment(position)
            findNavController().navigate(action)
        }
    }


    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )

            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                latLng = LatLng(lat, lon)


            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
                // todo :
            }
        }
    }


}



