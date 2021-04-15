/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.source.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.source.R
import id.co.app.source.databinding.ActivityMainBinding
import id.co.app.source.utilities.setupWithNavController

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNavigation
        val navGraphIds = listOf(
            R.navigation.navigation_home,
            R.navigation.navigation_feed,
            R.navigation.navigation_settings
        )
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
//        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
//        })
        currentNavController = controller
    }


    override fun onBackPressed() {

        val currentFragmentLabel = currentNavController?.value?.currentDestination?.label
        val fragmentHomeLabel = getString(R.string.home)
        if (currentFragmentLabel == fragmentHomeLabel) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            doubleBackToExitPressedOnce = true
            Alerter.create(this@MainActivity)
                .setTitle(R.string.exit_application)
                .setText(R.string.exit_confirm)
                .setBackgroundColorRes(R.color.alert_background_info)
                .setDuration(1000)
                .show()
            Handler(Looper.getMainLooper()).postDelayed(mRunnable, 2000)
        } else {
            super.onBackPressed()
        }
    }
}