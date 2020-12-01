package com.example.travelex.placeEdit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.database.TravelexDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceEditViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao
    private val photoModelDao = TravelexDatabase.getDatabase(application, viewModelScope).photoModelDao

    fun insert(placeWithPhotos: PlaceWithPhotos) {
        viewModelScope.launch(Dispatchers.IO){
            placeDao.update(placeWithPhotos.place)
        }
    }

    fun delete(placeWithPhotos: PlaceWithPhotos) {
        viewModelScope.launch(Dispatchers.IO){
            placeDao.delete(placeWithPhotos.place)
            photoModelDao.delete(placeWithPhotos.photos)
        }
    }
}