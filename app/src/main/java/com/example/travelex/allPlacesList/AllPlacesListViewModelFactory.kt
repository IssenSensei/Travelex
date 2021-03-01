package com.example.travelex.allPlacesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelex.database.PlaceDao
import java.lang.IllegalArgumentException

class AllPlacesListViewModelFactory(private val placeDao: PlaceDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllPlacesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllPlacesListViewModel(placeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}