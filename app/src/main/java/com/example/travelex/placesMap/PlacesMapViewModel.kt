package com.example.travelex.placesMap

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.travelex.database.PlaceDao
import com.example.travelex.database.PlaceWithPhotos

class PlacesMapViewModel(placeDao: PlaceDao) : ViewModel() {

    val allPlaces: LiveData<List<PlaceWithPhotos>> = placeDao.getAllPlaces()
}