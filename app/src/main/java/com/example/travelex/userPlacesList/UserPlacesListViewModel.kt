package com.example.travelex.userPlacesList

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

class UserPlacesListViewModel(application: Application) : AndroidViewModel(application) {

    val userPlacesList: LiveData<List<PlaceWithPhotos>>
    private val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao

    init {
        userPlacesList = placeDao.getUserPlaces(currentLoggedInUser.uid)
    }

    fun insert(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        placeDao.insert(place)
    }
}