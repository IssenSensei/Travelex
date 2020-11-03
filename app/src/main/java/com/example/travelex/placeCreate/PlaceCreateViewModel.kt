package com.example.travelex.placeCreate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.Place
import com.example.travelex.database.TravelexDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceCreateViewModel(application: Application) : AndroidViewModel(application) {

    val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao

    fun insert(place: Place) {
        viewModelScope.launch(Dispatchers.IO){
            placeDao.insert(place)
        }
    }
}