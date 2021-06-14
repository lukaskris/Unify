/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.source.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.forestry.plantation.core.extension.makeStatusBarTransparent
import com.forestry.plantation.core.extension.withDefault
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.source.R
import id.co.app.source.core.base.base.RootNavigation
import id.co.app.source.databinding.ActivityMainBinding
import id.co.app.source.home.ui.HomeFragmentDirections
import id.co.app.source.login.ui.LoginFragmentDirections
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RootNavigation {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var doubleBackToExitPressedOnce = false
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeStatusBarTransparent()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.previousBackStackEntry == null ||
            navController.currentBackStackEntry == null ||
            navController.currentBackStackEntry?.destination?.id == R.id.home_fragment
        ) {
            Timber.tag("Prefo").d("back pressed on Dashboard")
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
            Log.d(
                "MAIN",
                "Back pressed. Target: " + navController.previousBackStackEntry!!.destination.label
            )
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val backStackCount =
                navHostFragment?.childFragmentManager?.backStackEntryCount.withDefault()
            if (backStackCount > 1) {
                navController.navigateUp()
            } else {
                navController.navigate(
                    R.id.home_fragment, Bundle(),
                    NavOptions.Builder().setPopUpTo(R.id.home_fragment, true).build()
                )
            }
        }
    }

    override fun navigateToHome() {
        findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    override fun navigateToLogin() {
        findNavController(R.id.nav_host_fragment).navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
    }
}