package com.mhelrigo.cocktailmanual.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentIngredientDetailsBinding
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.drink.DrinkNavigator
import com.mhelrigo.cocktailmanual.ui.drink.DrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import com.mhelrigo.commons.NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * This will show a detailed info of an ingredient with the list of all related drinks
 * */
@AndroidEntryPoint
class IngredientDetailsFragment : BaseFragment<FragmentIngredientDetailsBinding>(), DrinkNavigator {
    private val ingredientViewModel: IngredientsViewModel by activityViewModels()
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var drinksRecyclerViewAdapter: DrinksRecyclerViewAdapter

    override fun inflateLayout(inflater: LayoutInflater): FragmentIngredientDetailsBinding =
        FragmentIngredientDetailsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        handleIngredient()
        handleDrinksFilteredByIngredient()

        requestData()

        if (isTablet!!) {
            refreshDrinksWhenItemToggled()
        }
    }

    private fun setUpRecyclerView() {
        drinksRecyclerViewAdapter = DrinksRecyclerViewAdapter()

        drinksRecyclerViewAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.setDrinkToBeSearched(drink.idDrink!!)
                navigateToDrinkDetail(
                    R.id.action_ingredientDetailsFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )

            }
            .launchIn(lifecycleScope)

        drinksRecyclerViewAdapter.toggleFavorite
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.toggleFavoriteOfADrink(drink)
                    .catch { throwable ->
                        Timber.e("Something went wrong sport... ${throwable.message}")
                    }
                    .collect {
                        drinksViewModel.setDrinkToBeSearched(it.idDrink!!)
                        drinksRecyclerViewAdapter.toggleFavoriteOfADrink(
                            it.bindingAdapterPosition,
                            it
                        )
                    }
            }
            .launchIn(lifecycleScope)

        binding.recyclerViewDrinks.apply {
            adapter = drinksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun requestForIngredient(p0: String) {
        ingredientViewModel.requestForIngredient(p0)
    }

    private fun handleIngredient() {
        ingredientViewModel.ingredient
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                processLoadingState(
                    state is ViewStateWrapper.Loading,
                    binding.imageViewIngredientLoading
                )

                when (state) {
                    is ViewStateWrapper.Loading -> {
                        binding.imageViewIngredientLoading.visibility = View.VISIBLE
                        binding.imageViewThumbnail.visibility = View.GONE
                        binding.textViewError.visibility = View.GONE
                        binding.textViewName.visibility = View.GONE
                        binding.textViewDescription.visibility = View.GONE
                        binding.recyclerViewDrinks.visibility = View.GONE
                    }
                    is ViewStateWrapper.Error -> {
                        binding.textViewError.visibility = View.VISIBLE
                        binding.imageViewThumbnail.visibility = View.GONE
                        binding.textViewName.visibility = View.GONE
                        binding.textViewDescription.visibility = View.GONE
                        binding.recyclerViewDrinks.visibility = View.GONE
                        binding.imageViewIngredientLoading.visibility = View.GONE
                    }
                    is ViewStateWrapper.Success -> {
                        binding.textViewError.visibility = View.GONE
                        binding.imageViewIngredientLoading.visibility = View.GONE
                        binding.textViewName.visibility = View.VISIBLE
                        binding.textViewDescription.visibility = View.VISIBLE
                        binding.recyclerViewDrinks.visibility = View.VISIBLE
                        binding.imageViewThumbnail.visibility = View.VISIBLE
                        binding.textViewName.text = state.data.strIngredient
                        binding.textViewDescription.text = state.data.strDescription
                        Glide.with(requireContext()).load(state.data.thumbNail())
                            .into(binding.imageViewThumbnail)

                        drinksViewModel.filterDrinksByIngredient(arguments?.getString(NAME)!!)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun handleDrinksFilteredByIngredient() {
        drinksViewModel.drinksFilteredByIngredient
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is ViewStateWrapper.Loading -> {
                        binding.recyclerViewDrinks.visibility = View.GONE
                    }
                    is ViewStateWrapper.Error -> {
                        binding.recyclerViewDrinks.visibility = View.GONE
                    }
                    is ViewStateWrapper.Success -> {
                        drinksRecyclerViewAdapter.differ.submitList(state.data)
                        binding.recyclerViewDrinks.visibility = View.VISIBLE
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    /**
     * Called to update the list of Meals on the left side of screen.
     * */
    private fun refreshDrinksWhenItemToggled() {
        drinksViewModel.toggledDrink
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                requestForIngredient(arguments?.getString(NAME)!!)
            }
            .launchIn(lifecycleScope)
    }

    override fun requestData() {
        super.requestData()
        requestForIngredient(arguments?.getString(NAME)!!)
    }
}