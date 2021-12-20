package com.example.covidd19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.covidd19.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bottom_nav_menu added 1 more items to improve the appearance
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        val navView: BottomNavigationView = binding.bottomNavigationView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setOnNavigationItemSelectedListener(navListener)
        // Home Fragment
        binding.fab.setOnClickListener { findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.homeFragment) }

    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.navigation_statictics ->{findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.statisticFragment)}
            R.id.navigation_assessment ->{findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.assessmentFragment)}
            R.id.navigation_mainAction ->{findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.mainActionFragment)}
            R.id.navigation_setting    ->{findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.settingFragment)}
        }
        return@OnNavigationItemSelectedListener true
    }
}