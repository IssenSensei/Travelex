package com.example.travelex.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDao{

    @Query("DELETE FROM places_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(place: Place)

    @Insert
    suspend fun insert(place: MutableList<Place>)

    @Query("SELECT * FROM places_table")
    fun getAllPlaces(): LiveData<List<Place>>


}