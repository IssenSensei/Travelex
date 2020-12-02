package com.example.travelex.placeEdit

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

class PlaceEditViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao = TravelexDatabase.getDatabase(application, viewModelScope).placeDao
    private val photoModelDao = TravelexDatabase.getDatabase(application, viewModelScope).photoModelDao
    var photos = mutableListOf<PhotoModel>()
    val photosLive = MutableLiveData<List<PhotoModel>>()

    lateinit var location: String

    fun update(placeWithPhotos: PlaceWithPhotos) {
        viewModelScope.launch(Dispatchers.IO){
            placeDao.update(placeWithPhotos.place)
            photoModelDao.deletePlacePhotos(placeWithPhotos.place.id)
            photos.forEach {
                it.placeId = placeWithPhotos.place.id
            }
            photoModelDao.insertNotExisting(photos)
        }
    }

    fun delete(placeWithPhotos: PlaceWithPhotos) {
        viewModelScope.launch(Dispatchers.IO){
            placeDao.delete(placeWithPhotos.place)
            photoModelDao.delete(placeWithPhotos.photos)
        }
    }

    fun savePhoto(photo: String) {
        photos.add(PhotoModel(0, 0, photo))
        photosLive.value = photos
    }

    fun removePhoto(photoModel: PhotoModel) {
        photos.remove(photoModel)
        photosLive.value = photos
    }
}