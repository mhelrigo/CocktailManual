package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkDetailsBinding
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.commons.ID
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

/**
 * This fragment will expanded information about a Drink.
 * It also have a capability of toggling favorites of said Drink.
 */
class DrinkDetailsFragment : BaseFragment<FragmentDrinkDetailsBinding>() {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    override fun inflateLayout(inflater: LayoutInflater): FragmentDrinkDetailsBinding =
        FragmentDrinkDetailsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewFavorite.setOnClickListener {
            val drink = (drinksViewModel.drinkDetails.value as ResultWrapper.Success<List<Drink>>)

            when (val result =
                drinksViewModel.toggleFavoriteOfADrink(drink.value[0])) {
                is ResultWrapper.Success -> {
                    requestForDrinkById()
                }
                is ResultWrapper.Error -> {
                    Timber.e("Something went wrong sport... ${result.error}")
                }
            }
        }

        binding.imageViewBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun onResume() {
        super.onResume()
        requestForDrinkById()
        handleDrinkDetails()
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun requestForDrinkById() {
        val id = arguments?.getString(ID)
        drinksViewModel.requestForDrinkDetailsByName(id)
    }

    private fun handleDrinkDetails() {
        drinksViewModel.drinkDetails.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding.imageViewDrinkLoading
            )
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.imageViewDrinkLoading.visibility = View.VISIBLE
                    binding.constraintLayoutRootSuccess.visibility = View.GONE
                }
                is ResultWrapper.Success -> {
                    binding.imageViewDrinkLoading.visibility = View.GONE
                    binding.constraintLayoutRootSuccess.visibility = View.VISIBLE
                    it.value[0]?.let { drink ->
                        binding.textViewName.text = drink.strDrink
                        binding.textViewShortDesc.text =
                            "${drink.strCategory} | ${drink.strAlcoholic} | ${drink.strGlass}"
                        binding.textViewMeasurements.text = drink.returnMeasurements()
                        binding.textViewIngredients.text =
                            drink.returnIngredients()
                        binding.textViewInstruction.text = drink.strInstructions

                        Glide.with(requireContext()).load(drink.strDrinkThumb).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(binding.imageViewThumbnail)

                        setUpFavoriteIcon(drink.returnIconForFavorite())
                    }
                }
                is ResultWrapper.Error -> {
                    binding.imageViewDrinkLoading.visibility = View.GONE
                    binding.constraintLayoutRootSuccess.visibility = View.VISIBLE
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
}