package com.example.travelex.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "photos")
data class PhotoModel(

    @PrimaryKey(autoGenerate = true)
    var photoID: Int = 0,

    var placeId: Int = 0,

    var photoUrl: String = ""

) : Serializable