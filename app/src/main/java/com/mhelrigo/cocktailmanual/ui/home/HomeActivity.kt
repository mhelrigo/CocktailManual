package com.mhelrigo.cocktailmanual.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

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