package com.example.travelex.placesMap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.database.TravelexDatabase

class PlacesMapViewModel(application: Application) : AndroidViewModel(application) {

    val allTasks: LiveData<List<PlaceWithPhotos>>

    private val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao

    init {
        allTasks = placeDao.getAllPlaces()
    }

}