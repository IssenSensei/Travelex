package com.example.travelex.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "places_table")
data class Place(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var name: String = "",

    var description: String = "",

    val location: String = "",

    var photo: String = "",

    var rating: Int = 0,

    var comment: String = "",

    var additionalInfo: String = ""
) : Serializable