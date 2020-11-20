package com.example.travelex.placesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.database.TravelexDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlacesListViewModel(application: Application) : AndroidViewModel(application) {

    val allTasks: LiveData<List<PlaceWithPhotos>>
    val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao

    init {
        allTasks = placeDao.getAllPlaces()
    }

    fun insert(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        placeDao.insert(place)
    }

}