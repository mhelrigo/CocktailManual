package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkListBinding
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 * This fragment showcase list of Drinks in terms of popularity and time uploaded.
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
    }

    private fun setUpRecyclerViews() {
        setUpLatestDrinkRecyclerView()
        setUpPopularDrinkRecyclerView()
        setUpRandomDrinkRecyclerView()
    }

    private fun setUpButtonClickListeners() {
        binding?.imageViewSearch?.setOnClickListener {
            navigateToSearchMeal()
        }

        binding?.imageViewRefresh?.setOnClickListener {
            drinksViewModel.requestForRandomDrinks()
        }
    }

    private fun setUpLatestDrinkRecyclerView() {
        latestDrinkAdapter = DrinksRecyclerViewAdapter()

        latestDrinkAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.setDrinkToBeSearched(drink)
                navigateToDrinkDetail(
                    R.id.action_drinkListFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        latestDrinkAdapter.toggleFavorite
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.toggleFavoriteOfADrink(drink)
                    .catch { throwable ->
                        Timber.e("Something went wrong sport... ${throwable.message}")
                    }.collect { data ->
                        drinksViewModel.setDrinkToBeSearched(data)
                    }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding?.recyclerViewLatestDrinks?.apply {
            adapter = latestDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun setUpPopularDrinkRecyclerView() {
        popularDrinkAdapter = DrinksRecyclerViewAdapter()

        popularDrinkAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.setDrinkToBeSearched(drink)
                navigateToDrinkDetail(
                    R.id.action_drinkListFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        popularDrinkAdapter.toggleFavorite
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.toggleFavoriteOfADrink(drink)
                    .catch { throwable ->
                        Timber.e("Something went wrong sport... ${throwable.message}")
                    }.collect { data ->
                        drinksViewModel.setDrinkToBeSearched(data)
                    }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding?.recyclerViewPopularDrinks?.apply {
            adapter = popularDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }


    private fun setUpRandomDrinkRecyclerView() {
        randomDrinkAdapter = DrinksRecyclerViewAdapter()

        randomDrinkAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.setDrinkToBeSearched(drink)
                navigateToDrinkDetail(
                    R.id.action_drinkListFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        randomDrinkAdapter.toggleFavorite
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.toggleFavoriteOfADrink(drink)
                    .catch { throwable ->
                        Timber.e("Something went wrong sport... ${throwable.message}")
                    }.collect { data ->
                        drinksViewModel.setDrinkToBeSearched(data)
                    }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding?.recyclerViewRandomDrinks?.apply {
            adapter = randomDrinkAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun handleLatestDrinks() {
        drinksViewModel.latestDrinks.observe(viewLifecycleOwner, { state ->
            processLoadingState(
                state is ViewStateWrapper.Loading,
                binding?.imageViewLatestDrinkLoading
            )

            when (state) {
                is ViewStateWrapper.Loading -> {
                    latestDrinkAdapter.submitList(emptyList())
                    binding?.imageViewLatestDrinkLoading?.visibility = View.VISIBLE
                    binding?.textViewErrorLatest?.visibility = View.INVISIBLE
                }

                is ViewStateWrapper.Error -> {
                    binding?.textViewErrorLatest?.visibility = View.VISIBLE
                    binding?.recyclerViewLatestDrinks?.visibility = View.INVISIBLE
                    binding?.imageViewLatestDrinkLoading?.visibility = View.INVISIBLE
                }
                is ViewStateWrapper.Success -> {
                    binding?.recyclerViewLatestDrinks?.visibility = View.VISIBLE
                    latestDrinkAdapter.submitList(state.data)
                    binding?.textViewErrorLatest?.visibility = View.INVISIBLE
                    binding?.imageViewLatestDrinkLoading?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handlePopularDrinks() {
        drinksViewModel.popularDrinks.observe(viewLifecycleOwner, { state ->
            processLoadingState(
                state is ViewStateWrapper.Loading,
                binding?.imageViewPopularDrinkLoading
            )

            when (state) {
                is ViewStateWrapper.Loading -> {
                    popularDrinkAdapter.submitList(emptyList())
                    binding?.imageViewPopularDrinkLoading?.visibility = View.VISIBLE
                    binding?.textViewErrorPopular?.visibility = View.INVISIBLE
                }
                is ViewStateWrapper.Error -> {
                    binding?.textViewErrorPopular?.visibility = View.VISIBLE
                    binding?.recyclerViewPopularDrinks?.visibility = View.INVISIBLE
                    binding?.imageViewPopularDrinkLoading?.visibility = View.INVISIBLE
                }
                is ViewStateWrapper.Success -> {
                    popularDrinkAdapter.submitList(state.data)
                    binding?.textViewErrorPopular?.visibility = View.INVISIBLE
                    binding?.imageViewPopularDrinkLoading?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handleRandomDrinks() {
        drinksViewModel.randomDrinks.observe(viewLifecycleOwner, { state ->
            processLoadingState(
                state is ViewStateWrapper.Loading,
                binding?.imageViewRandomDrinkLoading
            )

            when (state) {
                is ViewStateWrapper.Loading -> {
                    randomDrinkAdapter.submitList(emptyList())
                    binding?.imageViewRandomDrinkLoading?.visibility = View.VISIBLE
                    binding?.textViewErrorRandom?.visibility = View.INVISIBLE
                }
                is ViewStateWrapper.Error -> {
                    binding?.textViewErrorRandom?.visibility = View.VISIBLE
                    binding?.recyclerViewRandomDrinks?.visibility = View.INVISIBLE
                    binding?.imageViewRandomDrinkLoading?.visibility = View.INVISIBLE
                }
                is ViewStateWrapper.Success -> {
                    binding?.textViewErrorRandom?.visibility = View.INVISIBLE
                    binding?.recyclerViewRandomDrinks?.visibility = View.VISIBLE
                    randomDrinkAdapter.submitList(state.data)
                    binding?.imageViewRandomDrinkLoading?.visibility = View.INVISIBLE
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

    private fun navigateToSearchMeal() {
        findNavController().navigate(R.id.action_drinkListFragment_to_searchFragment)
    }

    override fun requestData() {
        super.requestData()
        if (drinksViewModel.latestDrinks.value?.noResultYet()!!) {
            requestForLatestDrinks()
        }

        if (drinksViewModel.popularDrinks.value?.noResultYet()!!) {
            requestForPopularDrinks()
        }

        if (drinksViewModel.randomDrinks.value?.noResultYet()!!) {
            requestForRandomDrinks()
        }
    }
}