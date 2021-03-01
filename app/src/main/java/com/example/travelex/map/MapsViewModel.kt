package com.example.travelex.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {

    var currentPosition: LatLng? = null
    var selectedPosition: LatLng? = null

}