package com.example.travelex.userPlacesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelex.MainActivity.Companion.currentLoggedInUser
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceDao
import com.example.travelex.database.PlaceWithPhotos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserPlacesListViewModel(private val placeDao: PlaceDao) : ViewModel() {

    val userPlacesList: LiveData<List<PlaceWithPhotos>> = placeDao.getUserPlaces(currentLoggedInUser.uid)

    fun insert(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        placeDao.insert(place)
    }
}