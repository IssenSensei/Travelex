package com.example.travelex

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.travelex.database.TravelexDatabase
import com.example.travelex.database.User
import com.example.travelex.placesList.PlacesListFragment
import com.example.travelex.profile.OnProfileEditListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), OnProfileEditListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance();
        MainScope().launch {
            currentLoggedInUser =
                TravelexDatabase.getDatabase(applicationContext, this).userDao.getUser(auth.uid)
        }.invokeOnCompletion {
            updateNavigationHeader()
            when (val currentFragment = (supportFragmentManager.primaryNavigationFragment as NavHostFragment)
                .childFragmentManager.primaryNavigationFragment) {
                is PlacesListFragment -> {
                    currentFragment.initializeListWithOtherPlaces()
                }
                else -> {

                }
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_places_list
            ), drawerLayout
        )

        setupNavigationMenu(navController)
        setupActionBar(navController, appBarConfiguration)
    }

    private fun updateNavigationHeader() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.findItem(R.id.nav_logout)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                Firebase.auth.signOut()
                finish()
                true
            }

        val header = navigationView.getHeaderView(0)
        header.header_email.text = currentLoggedInUser.userEmail
        header.header_name.text = currentLoggedInUser.userName
        Glide.with(baseContext).load(currentLoggedInUser.userPhoto)
            .placeholder(R.drawable.ic_profile).into(
                header.header_photo
            )
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        lateinit var currentLoggedInUser: User
    }

    override fun onProfileEdit(user: User) {
        updateNavigationHeader()
    }
}