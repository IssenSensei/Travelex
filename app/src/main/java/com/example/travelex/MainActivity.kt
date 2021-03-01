package com.example.travelex

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.travelex.database.TravelexDatabase
import com.example.travelex.database.User
import com.example.travelex.placeDetail.PlaceDetailFragmentDirections
import com.example.travelex.profile.LogoutListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), LogoutListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        MainScope().launch {
            currentLoggedInUser =
                TravelexDatabase.getDatabase(applicationContext, this).userDao.getUser(auth.uid)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_settings_profile,
                R.id.nav_places_map,
                R.id.nav_all_places_list,
                R.id.nav_user_places_list,
                R.id.nav_place_create
            )
        )
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        lateinit var currentLoggedInUser: User
    }

    override fun onLogOut() {
        auth.signOut()
        setResult(RESULT_CANCELED)
        finish()
    }
}