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
import com.mhelrigo.cocktailmanual.databinding.FragmentFavoritesBinding
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * FavoritesFragment will show the list of all favorite drinks.
 * */
@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(), DrinkNavigator {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var favoritesAdapter: DrinksRecyclerViewAdapter

    override fun inflateLayout(inflater: LayoutInflater): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        drinksViewModel.requestForFavoriteDrinks()
        handleFavorites()
    }

    private fun setUpRecyclerView() {
        favoritesAdapter = DrinksRecyclerViewAdapter()

        favoritesAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.setDrinkToBeSearched(drink.idDrink!!)
                navigateToDrinkDetail(
                    R.id.action_favoritesFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )
            }
            .launchIn(lifecycleScope)

        favoritesAdapter.toggleFavorite
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.toggleFavoriteOfADrink(drink)
                    .catch { throwable ->
                        Timber.e("Something went wrong sport... ${throwable.message}")
                    }
                    .collect { drink ->
                        drinksViewModel.setDrinkToBeSearched(drink.idDrink!!)
                        drinksViewModel.requestForFavoriteDrinks()
                    }
            }
            .launchIn(lifecycleScope)

        binding.recyclerViewFavorites.apply {
            adapter = favoritesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleFavorites() {
        drinksViewModel.favoriteDrinks.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ViewStateWrapper.Loading -> {
                    binding.recyclerViewFavorites.visibility = View.GONE
                    binding.textViewFavoritesError.visibility = View.GONE
                }
                is ViewStateWrapper.Error -> {
                    binding.recyclerViewFavorites.visibility = View.GONE
                    binding.textViewFavoritesError.visibility = View.VISIBLE
                }
                is ViewStateWrapper.Success -> {
                    favoritesAdapter.submitList(state.data)
                    if (state.data.isNotEmpty()) {
                        binding.recyclerViewFavorites.visibility = View.VISIBLE
                        binding.textViewFavoritesError.visibility = View.GONE
                    } else {
                        binding.textViewFavoritesError.visibility =
                            View.VISIBLE
                    }
                }
            }
        })
    }
}