package com.example.travelex.placesMap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelex.database.PlaceDao
import java.lang.IllegalArgumentException

class PlacesMapViewModelFactory(private val placeDao: PlaceDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesMapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesMapViewModel(placeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}