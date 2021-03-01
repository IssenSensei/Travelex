package com.example.travelex.userPlacesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelex.database.PlaceDao
import java.lang.IllegalArgumentException

class UserPlacesListViewModelFactory(private val placeDao: PlaceDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPlacesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserPlacesListViewModel(placeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}