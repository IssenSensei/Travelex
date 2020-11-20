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

    var location: String = "",

    var rating: Float = 0f,

    var comment: String = ""
) : Serializable