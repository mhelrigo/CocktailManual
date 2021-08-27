package com.mhelrigo.cocktailmanual.ui.home

import android.os.Bundle
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

        requestForLatestDrinks()
        requestForPopularDrinks()
        requestForRandomDrinks()

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

    private fun requestForLatestDrinks() {
        homeViewModel.requestForLatestDrinks()
    }

    private fun requestForPopularDrinks() {
        homeViewModel.requestForPopularDrinks()
    }

    private fun requestForRandomDrinks() {
        homeViewModel.requestForRandomDrinks()
    }
}