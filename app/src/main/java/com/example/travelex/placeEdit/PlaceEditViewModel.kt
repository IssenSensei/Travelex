package com.example.travelex.placeEdit

import androidx.lifecycle.*
import com.example.travelex.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceEditViewModel(private val placeDao: PlaceDao, private val photoModelDao: PhotoModelDao) : ViewModel() {

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