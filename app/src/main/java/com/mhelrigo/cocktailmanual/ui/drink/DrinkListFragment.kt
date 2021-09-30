package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkListBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.model.Drink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * This fragment showcase list of Drinks in terms of popularity and time uploaded.
 * Users can toggle favorites on each Drink item inside said lists.
 */
@AndroidEntryPoint
class DrinkListFragment : BaseFragment<FragmentDrinkListBinding>(), DrinkNavigator {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var popularDrinkAdapter: DrinksRecyclerViewAdapter
    private lateinit var latestDrinkAdapter: DrinksRecyclerViewAdapter
    private lateinit var randomDrinkAdapter: DrinksRecyclerViewAdapter

    override fun inflateLayout(inflater: LayoutInflater): FragmentDrinkListBinding =
        FragmentDrinkListBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButtonClickListeners()
        setUpRecyclerViews()

        handleLatestDrinks()
        handlePopularDrinks()
        handleRandomDrinks()

        requestForLatestDrinks()
        requestForPopularDrinks()

        if (isTablet!!) {
            refreshDrinksWhenItemToggled()
        }
    }

    private fun setUpRecyclerViews() {
        setUpLatestDrinkRecyclerView()
        setUpPopularDrinkRecyclerView()
        setUpRandomDrinkRecyclerView()
    }

    private fun setUpButtonClickListeners() {
        binding?.imageViewSearch?.setOnClickListener {
            searchDrinks()
        }

        binding?.imageViewRefresh?.setOnClickListener {
            drinksViewModel.requestForRandomDrinks()
        }
    }

    private fun setUpLatestDrinkRecyclerView() {
        latestDrinkAdapter =
            DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        drinksViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
                            drinksViewModel.setMealToBeSearched(item.idDrink!!)
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
                    drinksViewModel.setMealToBeSearched(item.idDrink!!)
                    navigateToDrinkDetail(
                        R.id.action_drinkListFragment_to_drinkDetailsFragment,
                        null,
                        findNavController(),
                        isTablet!!
                    )
                }
            })

        binding?.recyclerViewLatestDrinks?.apply {
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
                            drinksViewModel.setMealToBeSearched(item.idDrink!!)
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
                    drinksViewModel.setMealToBeSearched(item.idDrink!!)
                    navigateToDrinkDetail(
                        R.id.action_drinkListFragment_to_drinkDetailsFragment,
                        null,
                        findNavController(),
                        isTablet!!
                    )
                }
            })

        binding?.recyclerViewPopularDrinks?.apply {
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
                            drinksViewModel.setMealToBeSearched(item.idDrink!!)
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
                    drinksViewModel.setMealToBeSearched(item.idDrink!!)
                    navigateToDrinkDetail(
                        R.id.action_drinkListFragment_to_drinkDetailsFragment,
                        null,
                        findNavController(),
                        isTablet!!
                    )
                }
            })

        binding?.recyclerViewRandomDrinks?.apply {
            adapter = randomDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun handleLatestDrinks() {
        drinksViewModel.latestDrinks.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding?.imageViewLatestDrinkLoading
            )
            when (it) {
                is ResultWrapper.Success -> {
                    binding?.recyclerViewLatestDrinks?.visibility = View.VISIBLE
                    latestDrinkAdapter.differ.submitList(it.value)
                    binding?.textViewErrorLatest?.visibility = View.INVISIBLE
                    binding?.imageViewLatestDrinkLoading?.visibility = View.INVISIBLE
                }
                is ResultWrapper.Error -> {
                    binding?.textViewErrorLatest?.visibility = View.VISIBLE
                    binding?.recyclerViewLatestDrinks?.visibility = View.GONE
                    binding?.imageViewLatestDrinkLoading?.visibility = View.INVISIBLE
                }
                is ResultWrapper.Loading -> {
                    latestDrinkAdapter.differ.submitList(empty())
                    binding?.imageViewLatestDrinkLoading?.visibility = View.VISIBLE
                    binding?.textViewErrorLatest?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handlePopularDrinks() {
        drinksViewModel.popularDrinks.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding?.imageViewPopularDrinkLoading
            )
            when (it) {
                is ResultWrapper.Success -> {
                    binding?.recyclerViewPopularDrinks?.visibility = View.VISIBLE
                    popularDrinkAdapter.differ.submitList(it.value)
                    binding?.textViewErrorPopular?.visibility = View.INVISIBLE
                    binding?.imageViewPopularDrinkLoading?.visibility = View.INVISIBLE
                }
                is ResultWrapper.Error -> {
                    binding?.textViewErrorPopular?.visibility = View.VISIBLE
                    binding?.recyclerViewPopularDrinks?.visibility = View.GONE
                    binding?.imageViewPopularDrinkLoading?.visibility = View.INVISIBLE
                }
                is ResultWrapper.Loading -> {
                    popularDrinkAdapter.differ.submitList(empty())
                    binding?.imageViewPopularDrinkLoading?.visibility = View.VISIBLE
                    binding?.textViewErrorPopular?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handleRandomDrinks() {
        drinksViewModel.randomDrinks.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding?.imageViewRandomDrinkLoading
            )
            when (it) {
                is ResultWrapper.Success -> {
                    binding?.textViewErrorRandom?.visibility = View.INVISIBLE
                    binding?.recyclerViewRandomDrinks?.visibility = View.VISIBLE
                    randomDrinkAdapter.differ.submitList(it.value)
                    binding?.imageViewRandomDrinkLoading?.visibility = View.INVISIBLE
                }
                is ResultWrapper.Error -> {
                    binding?.textViewErrorRandom?.visibility = View.VISIBLE
                    binding?.recyclerViewRandomDrinks?.visibility = View.GONE
                    binding?.imageViewRandomDrinkLoading?.visibility = View.GONE
                }
                is ResultWrapper.Loading -> {
                    randomDrinkAdapter.differ.submitList(empty())
                    binding?.imageViewRandomDrinkLoading?.visibility = View.VISIBLE
                    binding?.textViewErrorRandom?.visibility = View.INVISIBLE
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

    private fun searchDrinks() {
        findNavController().navigate(R.id.action_drinkListFragment_to_searchFragment)
    }

    /**
     * Called to update the list of Meals on the left side of screen.
     * */
    private fun refreshDrinksWhenItemToggled() {
        CoroutineScope(mainCoroutine).launch {
            drinksViewModel.toggledDrink.collect {
                requestForLatestDrinks()
                requestForPopularDrinks()

                drinksViewModel.requestForRandomDrinks()
            }
        }
    }

    private fun empty() : Nothing? = null
}