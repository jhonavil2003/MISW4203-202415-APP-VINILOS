package com.example.app_vinilos_g17.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_vinilos_g17.R
import com.example.app_vinilos_g17.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_albums, R.id.navigation_artists, R.id.navigation_collectors
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#3D0F5E"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_albums -> {
                    navController.popBackStack(R.id.navigation_albums, false)
                    navController.navigate(R.id.navigation_albums)
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
