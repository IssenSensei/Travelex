package com.example.travelex.placeEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelex.database.PhotoModelDao
import com.example.travelex.database.PlaceDao
import java.lang.IllegalArgumentException

class PlaceEditViewModelFactory(private val placeDao: PlaceDao, private val photoModelDao: PhotoModelDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaceEditViewModel(placeDao, photoModelDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}