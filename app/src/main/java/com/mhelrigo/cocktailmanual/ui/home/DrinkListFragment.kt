package com.mhelrigo.cocktailmanual.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkListBinding
import com.mhelrigo.cocktailmanual.ui.home.adapter.HorizontalDrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.home.adapter.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.cocktailmanual.ui.model.FromCollectionOf
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

/**
 * This fragment showcase list of Drinks in terms of popularity and time uploaded.
 * Users can toggle favorites on each Drink item inside said lists.
 */
class DrinkListFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var drinkListBinding: FragmentDrinkListBinding

    private lateinit var popularDrinkAdapter: HorizontalDrinksRecyclerViewAdapter
    private lateinit var latestDrinkAdapter: HorizontalDrinksRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drinkListBinding = FragmentDrinkListBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return drinkListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        handleLatestDrinks()
        handlePopularDrinks()
        handleExpandedDrink()
    }

    private fun setUpRecyclerView() {
        latestDrinkAdapter =
            HorizontalDrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        homeViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
                            latestDrinkAdapter.toggleFavoriteOfADrink(
                                item.bindingAdapterPosition,
                                result.value
                            )
                        }
                        is ResultWrapper.Error -> {
                            Timber.e("Something went wrong sport...")
                        }
                    }
                }
            }, object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    expandDrinkDetails(item)
                }
            })

        drinkListBinding.recyclerViewLatestDrinks.apply {
            adapter = latestDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }

        popularDrinkAdapter =
            HorizontalDrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        homeViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
                            Timber.e("result.value ${result.value.isFavourite}")
                            popularDrinkAdapter.toggleFavoriteOfADrink(
                                item.bindingAdapterPosition,
                                result.value
                            )
                        }
                        is ResultWrapper.Error -> {
                            Timber.e("Something went wrong sport...")
                        }
                    }
                }
            }, object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    expandDrinkDetails(item)
                }
            })

        drinkListBinding.recyclerViewPopularDrinks.apply {
            adapter = popularDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun handleLatestDrinks() {
        homeViewModel.latestDrinks.observe(viewLifecycleOwner, {
            it?.let { drinks ->
                latestDrinkAdapter.submit(drinks)
            }
        })
    }

    private fun handlePopularDrinks() {
        homeViewModel.popularDrinks.observe(viewLifecycleOwner, {
            it?.let { drinks ->
                popularDrinkAdapter.submit(drinks)
            }
        })
    }

    private fun handleExpandedDrink() {
        homeViewModel.expandedDrinkDetails.observe(viewLifecycleOwner, {
            it?.let {
                Timber.e("Drink $it")
                when (it.fromCollectionOf!!) {
                    FromCollectionOf.LATEST -> {
                        latestDrinkAdapter.toggleFavoriteOfADrink(it.bindingAdapterPosition, it)
                    }
                    FromCollectionOf.POPULAR -> {
                        popularDrinkAdapter.toggleFavoriteOfADrink(it.bindingAdapterPosition, it)
                    }
                }
            }
        })
    }

    private fun expandDrinkDetails(drink: Drink) {
        homeViewModel.expandDrinkDetails(drink)
        findNavController().navigate(R.id.action_drinkListFragment_to_drinkDetailsFragment)
    }
}