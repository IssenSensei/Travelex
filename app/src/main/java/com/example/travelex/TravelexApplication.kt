package com.example.travelex

import android.app.Application
import com.example.travelex.database.TravelexDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TravelexApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { TravelexDatabase.getDatabase(this, applicationScope) }
    val placeDao by lazy { database.placeDao }
    val userDao by lazy { database.userDao }
    val photoModelDao by lazy { database.photoModelDao }
}