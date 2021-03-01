package com.example.travelex.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelex.database.User
import com.example.travelex.database.UserDao
import kotlinx.coroutines.launch

class ProfileViewModel(private val userDao: UserDao) : ViewModel() {

    var photo : String = ""

    fun updateUser(currentLoggedInUser: User) {
        viewModelScope.launch {
            userDao.updateUser(currentLoggedInUser)
        }
    }
}