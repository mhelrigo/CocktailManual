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
import com.mhelrigo.cocktailmanual.ui.home.adapter.VerticalDrinksRecyclerViewAdapter
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

    private var drinkListBinding: FragmentDrinkListBinding? = null

    private lateinit var popularDrinkAdapter: HorizontalDrinksRecyclerViewAdapter
    private lateinit var latestDrinkAdapter: HorizontalDrinksRecyclerViewAdapter
    private lateinit var randomDrinkAdapter: VerticalDrinksRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (drinkListBinding == null) {
            drinkListBinding = FragmentDrinkListBinding.inflate(layoutInflater)
        }

        // Inflate the layout for this fragment
        return drinkListBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinkListBinding?.imageViewRefresh?.setOnClickListener {
            homeViewModel.requestForRandomDrinks()
        }

        setUpRecyclerViews()

        handleLatestDrinks()
        handlePopularDrinks()
        handleRandomDrinks()
        handleExpandedDrink()
    }

    private fun setUpRecyclerViews() {
        setUpLatestDrinkRecyclerView()
        setUpPopularDrinkRecyclerView()
        setUpRandomDrinkRecyclerView()
    }

    private fun setUpLatestDrinkRecyclerView() {
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

        drinkListBinding?.recyclerViewLatestDrinks?.apply {
            adapter = latestDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun setUpPopularDrinkRecyclerView() {
        popularDrinkAdapter =
            HorizontalDrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        homeViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
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

        drinkListBinding?.recyclerViewPopularDrinks?.apply {
            adapter = popularDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun setUpRandomDrinkRecyclerView() {
        randomDrinkAdapter =
            VerticalDrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        homeViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
                            randomDrinkAdapter.toggleFavoriteOfADrink(
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

        drinkListBinding?.recyclerViewRandomDrinks?.apply {
            adapter = randomDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun handleLatestDrinks() {
        homeViewModel.latestDrinks.observe(viewLifecycleOwner, {
            it?.let { drinks ->
                latestDrinkAdapter.differ.submitList(drinks)
            }
        })
    }

    private fun handlePopularDrinks() {
        homeViewModel.popularDrinks.observe(viewLifecycleOwner, {
            it?.let { drinks ->
                popularDrinkAdapter.differ.submitList(drinks)
            }
        })
    }

    private fun handleRandomDrinks() {
        homeViewModel.randomDrinks.observe(viewLifecycleOwner, {
            it?.let { drinks ->
                randomDrinkAdapter.differ.submitList(drinks.toList())
            }
        })
    }

    private fun handleExpandedDrink() {
        homeViewModel.expandedDrinkDetails.observe(viewLifecycleOwner, {
            it?.let {
                when (it.fromCollectionOf!!) {
                    FromCollectionOf.LATEST -> {
                        latestDrinkAdapter.toggleFavoriteOfADrink(it.bindingAdapterPosition, it)
                    }
                    FromCollectionOf.POPULAR -> {
                        popularDrinkAdapter.toggleFavoriteOfADrink(it.bindingAdapterPosition, it)
                    }
                    FromCollectionOf.RANDOM -> {
                        randomDrinkAdapter.toggleFavoriteOfADrink(it.bindingAdapterPosition, it)
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