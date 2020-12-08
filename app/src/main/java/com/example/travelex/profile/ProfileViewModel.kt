package com.example.travelex.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.TravelexDatabase
import com.example.travelex.database.User
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao =
        TravelexDatabase.getDatabase(application, viewModelScope).userDao

    fun updateUser(currentLoggedInUser: User) {
        viewModelScope.launch {
            userDao.updateUser(currentLoggedInUser)
        }
    }

    var photo : String = ""

}