package com.example.travelex.placeCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelex.database.PhotoModelDao
import com.example.travelex.database.PlaceDao
import java.lang.IllegalArgumentException

class PlaceCreateViewModelFactory(private val placeDao: PlaceDao, private val photoModelDao: PhotoModelDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaceCreateViewModel(placeDao, photoModelDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}