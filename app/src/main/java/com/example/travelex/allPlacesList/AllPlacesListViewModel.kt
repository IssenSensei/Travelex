package com.example.travelex.allPlacesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.travelex.database.PlaceDao
import com.example.travelex.database.PlaceWithPhotos

class AllPlacesListViewModel(placeDao: PlaceDao) : ViewModel() {

    val placesList: LiveData<List<PlaceWithPhotos>> = placeDao.getAllPlaces()

}