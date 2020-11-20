package com.example.travelex.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao{

    @Query("DELETE FROM places_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(place: Place): Long

    @Insert
    suspend fun insert(place: MutableList<Place>)

    @Transaction
    @Query("SELECT * FROM places_table")
    fun getAllPlaces(): LiveData<List<PlaceWithPhotos>>

    @Update
    fun update(place: Place)


}