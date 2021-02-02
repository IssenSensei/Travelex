package com.example.travelex.allPlacesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelex.MainActivity.Companion.currentLoggedInUser
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.database.TravelexDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllPlacesListViewModel(application: Application) : AndroidViewModel(application) {

    val placesList: LiveData<List<PlaceWithPhotos>>

    init {
        val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao
        placesList = placeDao.getAllPlaces()
    }

}