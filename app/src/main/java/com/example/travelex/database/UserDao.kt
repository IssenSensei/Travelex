package com.example.travelex.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao{

    @Query("DELETE FROM users_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users_table WHERE uid = :uid")
    suspend fun getUser(uid: String?): User

    @Update
    suspend fun updateUser(currentLoggedInUser: User)
}