package com.mhelrigo.cocktailmanual.ui.drink

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
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.model.Drink
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

/**
 * This fragment showcase list of Drinks in terms of popularity and time uploaded.
 * Users can toggle favorites on each Drink item inside said lists.
 */
class DrinkListFragment : Fragment() {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private var drinkListBinding: FragmentDrinkListBinding? = null

    private lateinit var popularDrinkAdapter: DrinksRecyclerViewAdapter
    private lateinit var latestDrinkAdapter: DrinksRecyclerViewAdapter
    private lateinit var randomDrinkAdapter: DrinksRecyclerViewAdapter

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
            drinksViewModel.requestForRandomDrinks()
        }

        setUpRecyclerViews()

        handleLatestDrinks()
        handlePopularDrinks()
        handleRandomDrinks()
    }

    override fun onResume() {
        super.onResume()
        requestForLatestDrinks()
        requestForPopularDrinks()
        requestForRandomDrinks()
    }

    private fun setUpRecyclerViews() {
        setUpLatestDrinkRecyclerView()
        setUpPopularDrinkRecyclerView()
        setUpRandomDrinkRecyclerView()
    }

    private fun setUpLatestDrinkRecyclerView() {
        latestDrinkAdapter =
            DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        drinksViewModel.toggleFavoriteOfADrink(item)) {
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
            DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        drinksViewModel.toggleFavoriteOfADrink(item)) {
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
            DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        drinksViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
                            randomDrinkAdapter.toggleFavoriteOfADrink(
                                item.bindingAdapterPosition,
                                result.value
                            )
                        }
                        is ResultWrapper.Error -> {
                            Timber.e("${result.error}")
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
        drinksViewModel.latestDrinks.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    drinkListBinding?.recyclerViewLatestDrinks?.visibility = View.VISIBLE
                    latestDrinkAdapter.differ.submitList(it.value)
                    drinkListBinding?.textViewErrorLatest?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutLatestDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutLatestDrinks?.stopShimmer()
                }
                is ResultWrapper.Error -> {
                    drinkListBinding?.textViewErrorLatest?.visibility = View.VISIBLE
                    drinkListBinding?.recyclerViewLatestDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutLatestDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutLatestDrinks?.stopShimmer()
                }
                is ResultWrapper.Loading -> {
                    drinkListBinding?.shimmerFrameLayoutLatestDrinks?.visibility = View.VISIBLE
                    drinkListBinding?.textViewErrorLatest?.visibility = View.INVISIBLE
                    drinkListBinding?.recyclerViewLatestDrinks?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handlePopularDrinks() {
        drinksViewModel.popularDrinks.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    drinkListBinding?.recyclerViewPopularDrinks?.visibility = View.VISIBLE
                    popularDrinkAdapter.differ.submitList(it.value)
                    drinkListBinding?.textViewErrorPopular?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutPopularDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutPopularDrinks?.stopShimmer()
                }
                is ResultWrapper.Error -> {
                    drinkListBinding?.textViewErrorPopular?.visibility = View.VISIBLE
                    drinkListBinding?.recyclerViewPopularDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutPopularDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutPopularDrinks?.stopShimmer()
                }
                is ResultWrapper.Loading -> {
                    drinkListBinding?.shimmerFrameLayoutPopularDrinks?.visibility = View.VISIBLE
                    drinkListBinding?.textViewErrorPopular?.visibility = View.INVISIBLE
                    drinkListBinding?.recyclerViewPopularDrinks?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handleRandomDrinks() {
        drinksViewModel.randomDrinks.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    drinkListBinding?.textViewErrorRandom?.visibility = View.INVISIBLE
                    drinkListBinding?.recyclerViewRandomDrinks?.visibility = View.VISIBLE
                    randomDrinkAdapter.differ.submitList(it.value)
                    drinkListBinding?.shimmerFrameLayoutRandomDrinks?.visibility = View.INVISIBLE
                    drinkListBinding?.shimmerFrameLayoutRandomDrinks?.stopShimmer()
                }
                is ResultWrapper.Error -> {
                    drinkListBinding?.textViewErrorRandom?.visibility = View.VISIBLE
                    drinkListBinding?.recyclerViewRandomDrinks?.visibility = View.GONE
                    drinkListBinding?.shimmerFrameLayoutRandomDrinks?.visibility = View.GONE
                    drinkListBinding?.shimmerFrameLayoutRandomDrinks?.stopShimmer()
                }
                is ResultWrapper.Loading -> {
                    drinkListBinding?.shimmerFrameLayoutRandomDrinks?.visibility = View.VISIBLE
                    drinkListBinding?.textViewErrorRandom?.visibility = View.INVISIBLE
                    drinkListBinding?.recyclerViewRandomDrinks?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun requestForLatestDrinks() {
        drinksViewModel.requestForLatestDrinks()
    }

    private fun requestForPopularDrinks() {
        drinksViewModel.requestForPopularDrinks()
    }

    private fun requestForRandomDrinks() {
        drinksViewModel.requestForRandomDrinks()
    }

    private fun expandDrinkDetails(drink: Drink) {
        drinksViewModel.expandDrinkDetails(drink)
        findNavController().navigate(R.id.action_drinkListFragment_to_drinkDetailsFragment)
    }
}