package com.example.travelex.placeCreate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.PhotoModel
import com.example.travelex.database.PhotoModelDao
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceCreateViewModel(private val placeDao: PlaceDao, private val photoModelDao: PhotoModelDao) : ViewModel() {

    val photos = mutableListOf<PhotoModel>()
    val photosLive = MutableLiveData<List<PhotoModel>>()
    var location: String? = null

    fun insert(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            val placeId = placeDao.insert(place)
            photos.forEach {
                it.placeId = placeId.toInt()
            }
            photoModelDao.insert(photos)
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