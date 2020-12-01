package com.example.travelex.placeDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.PhotoModel
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.database.TravelexDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceDetailViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var placeWithPhotos: PlaceWithPhotos

}