package com.mhelrigo.cocktailmanual.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()

        handleInternetConnectionChanges()
    }

    override fun onResume() {
        super.onResume()
        registerConnectivityListener()
    }

    override fun onPause() {
        super.onPause()
        unRegisterConnectivityListener()
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        settingsViewModel.isNightMode.observe(this, {
            it?.let {
                if (it) {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        })
    }

    private fun handleInternetConnectionChanges() {
        homeViewModel.isConnectedToInternet.observe(this, {
            it?.let {
                if (it) {
                    binding.linearLayoutNoInternetConnection.visibility = View.GONE
                } else {
                    binding.linearLayoutNoInternetConnection.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun registerConnectivityListener() {
        registerReceiver(
            connectivityListener,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun unRegisterConnectivityListener() {
        unregisterReceiver(connectivityListener)
    }

    private val connectivityListener = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val connectivityManager: ConnectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            homeViewModel.handleInternetConnectionChanges(networkInfo != null && networkInfo.isConnectedOrConnecting)
        }
    }
}