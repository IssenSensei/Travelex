package com.example.travelex.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PhotoModelDao{

    @Query("DELETE FROM photos")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(photoModel: PhotoModel)

    @Insert
    suspend fun insert(photoModel: MutableList<PhotoModel>)

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): LiveData<List<PhotoModel>>

    @Update
    fun update(photoModel: PhotoModel)

    @Delete
    fun delete(photos: List<PhotoModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotExisting(photoModel: MutableList<PhotoModel>)

}