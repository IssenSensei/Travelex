package com.example.travelex.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    var currentPosition: LatLng? = null
    var selectedPosition: LatLng? = null

}