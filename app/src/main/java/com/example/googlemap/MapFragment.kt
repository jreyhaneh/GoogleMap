package com.example.googlemap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.googlemap.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private var mGoogleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(layoutInflater, container, false)


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

        val latLon1 = LatLng(41.077577, 28.918875)
        val latLon2 = LatLng(35.029791, 58.054209)

        val markerView = layoutInflater.inflate(
            R.layout.market_layout, binding.root as ViewGroup,
            false
        )
        val text = markerView.findViewById<TextView>(R.id.textMarker)
        val cardView = markerView.findViewById<CardView>(R.id.markerCardView)

        text.text = "I am here"
        val bitmap1 = Bitmap.createScaledBitmap(
            viewToBitmap(cardView)!!,
            cardView.width,
            cardView.height,
            false
        )
        val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap1)
        var marker = mGoogleMap!!.addMarker(MarkerOptions().position(latLon1).icon(smallMarkerIcon))

        mGoogleMap!!.setOnMapClickListener { location ->
            marker!!.remove()
            marker =
                mGoogleMap!!.addMarker(MarkerOptions().position(location).icon(smallMarkerIcon))
        }

        binding.buttonSet.setOnClickListener {
            val latitude = marker!!.position.latitude.toFloat()
            val longitude = marker!!.position.longitude.toFloat()
            val position = floatArrayOf(latitude,longitude)

            val action = MapFragmentDirections.actionMapFragmentToFinallyFragment(position)
            findNavController().navigate(action)
        }


//        text.text = "I am here too"
//        val bitmap2 = Bitmap.createScaledBitmap(viewToBitmap(cardView)!!,cardView.width,cardView.height,false)
//        val smallMarkerIcon2=BitmapDescriptorFactory.fromBitmap(bitmap2)
        mGoogleMap!!.addMarker(MarkerOptions().position(latLon2))
    }

    private fun viewToBitmap(view: View): Bitmap {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitMap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitMap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitMap
    }

}



