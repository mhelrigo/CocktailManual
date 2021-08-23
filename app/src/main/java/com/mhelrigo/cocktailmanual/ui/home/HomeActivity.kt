package com.mhelrigo.cocktailmanual.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.databinding.ActivityHomeBinding
import com.mhelrigo.cocktailmanual.ui.home.adapter.HorizontalDrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.home.adapter.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.model.Drink
import dagger.hilt.android.AndroidEntryPoint
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityHomeBinding

    private lateinit var popularDrinkAdapter: HorizontalDrinksRecyclerViewAdapter
    private lateinit var latestDrinkAdapter: HorizontalDrinksRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()

        requestForLatestDrinks()
        handleLatestDrinks()

        requestForPopularDrinks()
        handlePopularDrinks()
    }

    private fun setUpRecyclerView() {
        latestDrinkAdapter = HorizontalDrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
            override fun onClick(item: Pair<Int, Drink>) {
                when (val result = homeViewModel.toggleFavoriteOfADrink(item.second.toDrinkDomainModel())){
                    is ResultWrapper.Success -> {
                        latestDrinkAdapter.toggleFavoriteOfADrink(item.first, result.value)
                    }
                    is ResultWrapper.Error -> {
                        Timber.e("Something went wrong sport...")
                    }
                }
            }
        })

        binding.recyclerViewLatestDrinks.apply {
            adapter = latestDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }

        popularDrinkAdapter = HorizontalDrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
            override fun onClick(item: Pair<Int, Drink>) {
                when (val result = homeViewModel.toggleFavoriteOfADrink(item.second.toDrinkDomainModel())){
                    is ResultWrapper.Success -> {
                        Timber.e("result.value ${result.value.isFavourite}")
                        popularDrinkAdapter.toggleFavoriteOfADrink(item.first, result.value)
                    }
                    is ResultWrapper.Error -> {
                        Timber.e("Something went wrong sport...")
                    }
                }
            }
        })

        binding.recyclerViewPopularDrinks.apply {
            adapter = popularDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun handleLatestDrinks() {
        homeViewModel.latestDrinks.observe(this, {
            it?.let { drinks ->
                latestDrinkAdapter.submit(drinks)
            }
        })
    }

    private fun handlePopularDrinks() {
        homeViewModel.popularDrinks.observe(this, {
            it?.let { drinks ->
                popularDrinkAdapter.submit(drinks)
            }
        })
    }

    private fun requestForLatestDrinks() {
        homeViewModel.fetchLatestDrinks()
    }

    private fun requestForPopularDrinks() {
        homeViewModel.fetchPopularDrinks()
    }
}