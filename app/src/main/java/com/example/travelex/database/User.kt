package com.example.travelex.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users_table")
data class User(

    @PrimaryKey
    var uid: String,

    var userName: String = "",

    var userEmail: String = "",

    var userPhoto: String = ""

) : Serializable