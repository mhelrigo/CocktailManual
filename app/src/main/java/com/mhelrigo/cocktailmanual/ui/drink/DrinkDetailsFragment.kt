package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkDetailsBinding
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 * This fragment will show information about a Drink.
 */
@AndroidEntryPoint
class DrinkDetailsFragment : BaseFragment<FragmentDrinkDetailsBinding>() {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    override fun inflateLayout(inflater: LayoutInflater): FragmentDrinkDetailsBinding =
        FragmentDrinkDetailsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToggleFavoriteButton()

        handleDrinkDetails()
        requestForDrinkById()
    }

    private fun setUpToggleFavoriteButton() {
        binding.imageViewFavorite.setOnClickListener {
            val drink = (drinksViewModel.drinkDetails.value as ViewStateWrapper.Success)
            drinksViewModel.toggleFavoriteOfADrink(drink.data[0])
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .onEach { data ->
                    drinksViewModel.syncMeals(data, SYNC_MEALS_DEFAULT_INDEX)
                    drinksViewModel.requestForFavoriteDrinks()
                    drinksViewModel.setDrinkToBeSearched(data)
                }.catch { throwable ->
                    Timber.e("Something went wrong sport... ${throwable.message}")
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun requestForDrinkById() {
        drinksViewModel.drinkToBeSearched.observe(viewLifecycleOwner, { data ->
            drinksViewModel.requestForDrinkDetailsByName(data)
        })
    }

    private fun handleDrinkDetails() {
        drinksViewModel.drinkDetails.observe(viewLifecycleOwner, { state ->
            processLoadingState(
                state is ViewStateWrapper.Loading,
                binding.imageViewDrinkLoading
            )

            when (state) {
                is ViewStateWrapper.Init -> {
                    binding.textViewEmptyDrink.visibility = View.VISIBLE
                }
                is ViewStateWrapper.Loading -> {
                    binding.imageViewDrinkLoading.visibility = View.VISIBLE
                    binding.constraintLayoutRootSuccess.visibility = View.GONE
                    binding.textViewEmptyDrink.visibility = View.GONE
                }
                is ViewStateWrapper.Error -> {
                    binding.imageViewDrinkLoading.visibility = View.GONE
                    binding.constraintLayoutRootSuccess.visibility = View.GONE
                    binding.textViewEmptyDrink.visibility = View.VISIBLE
                }
                is ViewStateWrapper.Success -> {
                    binding.imageViewDrinkLoading.visibility = View.GONE
                    binding.constraintLayoutRootSuccess.visibility = View.VISIBLE
                    binding.textViewEmptyDrink.visibility = View.GONE
                    state.data[0].let { drink ->
                        binding.textViewName.text = drink.strDrink
                        binding.textViewShortDesc.text =
                            "${drink.strCategory} | ${drink.strAlcoholic} | ${drink.strGlass}"
                        binding.textViewIngredients.text =
                            drink.returnIngredientWithMeasurement()
                        binding.textViewInstruction.text = drink.strInstructions

                        Glide.with(requireContext()).load(drink.strDrinkThumb)
                            .diskCacheStrategy(
                                DiskCacheStrategy.ALL
                            ).into(binding.imageViewThumbnail)

                        setUpFavoriteIcon(drink.returnIconForFavorite())
                    }
                }
            }
        })
    }

    private fun setUpFavoriteIcon(resourceDrawable: Int) {
        binding.imageViewFavorite.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                resourceDrawable,
                null
            )
        )
    }

    override fun requestData() {
        super.requestData()
        if (drinksViewModel.drinkDetails.value?.noResultYet()!!) {
            drinksViewModel.drinkToBeSearched.value?.let {
                drinksViewModel.requestForDrinkDetailsByName(
                    it
                )
            }
        }
    }
}