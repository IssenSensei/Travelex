package com.example.travelex.placesMap

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.travelex.R
import com.example.travelex.misc.latLngToString

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PlacesMapFragment : Fragment() {

    private lateinit var placesMapViewModel: PlacesMapViewModel

    private val callback = OnMapReadyCallback { googleMap ->
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        placesMapViewModel.allTasks.observe(this, {
            it.forEach {

                val location = it.place.latLng.split(",".toRegex()).toTypedArray()
                val latLng = LatLng(location[0].toDouble(), location[1].toDouble())

                googleMap.addMarker(
                    MarkerOptions().position(latLng).title(it.place.name)
                        .snippet(it.place.description)
                        .icon(
                            if (it.place.userId == 0) {
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                            } else {
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                            }
                        )
                )
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        placesMapViewModel = ViewModelProvider(this).get(PlacesMapViewModel::class.java)
        return inflater.inflate(R.layout.fragment_places_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.places_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}