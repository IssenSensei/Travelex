package com.example.travelex.placesList

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

class PlacesListViewModel(application: Application) : AndroidViewModel(application) {

//    val allTasks: LiveData<List<PlaceWithPhotos>>

    var mediatorLiveData: MediatorLiveData<List<PlaceWithPhotos>> = MediatorLiveData<List<PlaceWithPhotos>>()

    //            = MediatorLiveData<List<PlaceWithPhotos>>()
    private val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao

    ////        allTasks = placeDao.getAllPlaces()
////        mediatorLiveData.addSource(placeDao.getOtherPlaces(0)) { value -> mediatorLiveData.setValue(value) }
//        mediatorLiveData.addSource(placeDao.getUserPlaces("currentLoggedInUser.uid")) { value ->
//            mediatorLiveData.setValue(
//                value
//            )
//        }
//    }

    fun insert(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        placeDao.insert(place)
    }

    fun getOtherPlaces() {
        mediatorLiveData.addSource(placeDao.getAllPlaces()) { value ->
            mediatorLiveData.setValue(
                value
            )
        }
        mediatorLiveData.removeSource(placeDao.getUserPlaces(currentLoggedInUser.uid))
    }
    fun getUserPlaces() {
        mediatorLiveData.addSource(placeDao.getUserPlaces(currentLoggedInUser.uid)) { value ->
            mediatorLiveData.setValue(
                value
            )
        }
        mediatorLiveData.removeSource(placeDao.getOtherPlaces(currentLoggedInUser.uid))
    }


}