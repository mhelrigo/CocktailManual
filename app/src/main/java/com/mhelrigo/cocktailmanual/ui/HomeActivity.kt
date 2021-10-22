package com.mhelrigo.cocktailmanual.ui

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.ActivityHomeBinding
import com.mhelrigo.cocktailmanual.di.AppModule.IS_TABLET
import com.mhelrigo.cocktailmanual.ui.drink.DrinkDetailsFragment
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import com.mhelrigo.cocktailmanual.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val drinksViewModel: DrinksViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    @JvmField
    @field:[Inject Named(IS_TABLET)]
    var isTablet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()

        handleNightModeSwitchChanges()
        handleInternetConnectionChanges()

        requestForRandomDrinks()

        setUpViewForFragment(savedInstanceState)

        settingsViewModel.requestForSettings()
    }

    private fun setUpViewForFragment(p0: Bundle?) {
        if (isTablet) {
            if (p0 == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.frameLayoutDrinkDetail,
                        DrinkDetailsFragment(),
                        DrinkDetailsFragment::class.java.simpleName
                    ).commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerConnectivityListener()
    }

    override fun onPause() {
        super.onPause()
        unRegisterConnectivityListener()
    }

    private fun handleNightModeSwitchChanges() {
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

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun handleInternetConnectionChanges() {
        settingsViewModel.isNetworkAvailable
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { data ->
                if (data) {
                    binding.linearLayoutNoInternetConnection.visibility = View.GONE
                } else {
                    binding.linearLayoutNoInternetConnection.visibility = View.VISIBLE
                }
            }
            .launchIn(lifecycleScope)
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

            settingsViewModel.changeNetworkAvailability(networkInfo != null && networkInfo.isConnectedOrConnecting)
        }
    }

    private fun requestForRandomDrinks() {
        drinksViewModel.requestForRandomDrinks()
    }
}