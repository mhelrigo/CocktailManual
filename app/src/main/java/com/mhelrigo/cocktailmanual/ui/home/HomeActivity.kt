package com.mhelrigo.cocktailmanual.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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