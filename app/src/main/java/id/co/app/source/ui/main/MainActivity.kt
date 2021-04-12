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
import androidx.navigation.ui.setupWithNavController
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.source.R
import id.co.app.source.databinding.MainActivityBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavController: NavController? = null
    private lateinit var binding: MainActivityBinding
    private var doubleBackToExitPressedOnce = false
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.main_activity)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.navigateUp() ?: false
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNavigation
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        bottomNavigationView.setupWithNavController(navController)

        currentNavController = navController
        // Whenever the selected controller changes, setup the action bar.
//        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
//        })
    }

    override fun onBackPressed() {

        val currentFragmentLabel = currentNavController?.currentDestination?.label
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