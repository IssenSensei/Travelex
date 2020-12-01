package com.example.travelex.database

import androidx.room.*
import java.io.Serializable

data class PlaceWithPhotos(
    @Embedded
    val place: Place,
    @Relation(
        parentColumn = "id",
        entityColumn = "placeId"
    )
    val photos: MutableList<PhotoModel>,

) : Serializable
