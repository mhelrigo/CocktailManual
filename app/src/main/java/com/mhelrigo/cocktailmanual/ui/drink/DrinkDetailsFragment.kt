package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkDetailsBinding
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.cocktailmanual.ui.setUpDeviceBackNavigation
import com.mhelrigo.commons.ID
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

/**
 * This fragment will expanded information about a Drink.
 * It also have a capability of toggling favorites of said Drink.
 */
class DrinkDetailsFragment : Fragment() {

    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var drinkDetailsBinding: FragmentDrinkDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drinkDetailsBinding = FragmentDrinkDetailsBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return drinkDetailsBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpDeviceBackNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drinkDetailsBinding.imageViewFavorite.setOnClickListener {
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

        drinkDetailsBinding.imageViewBack.setOnClickListener {
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
            when (it) {
                is ResultWrapper.Loading -> {
                    drinkDetailsBinding.constraintLayoutRootLoading.visibility = View.VISIBLE
                    drinkDetailsBinding.constraintLayoutRootSuccess.visibility = View.GONE
                }
                is ResultWrapper.Success -> {
                    drinkDetailsBinding.constraintLayoutRootLoading.visibility = View.GONE
                    drinkDetailsBinding.constraintLayoutRootSuccess.visibility = View.VISIBLE
                    it.value[0]?.let { drink ->
                        drinkDetailsBinding.textViewName.text = drink.strDrink
                        drinkDetailsBinding.textViewShortDesc.text =
                            "${drink.strCategory} | ${drink.strAlcoholic} | ${drink.strGlass}"
                        drinkDetailsBinding.textViewMeasurements.text = drink.returnMeasurements()
                        drinkDetailsBinding.textViewIngredients.text =
                            drink.returnIngredients()
                        drinkDetailsBinding.textViewInstruction.text = drink.strInstructions

                        Glide.with(requireContext()).load(drink.strDrinkThumb).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(drinkDetailsBinding.imageViewThumbnail)

                        setUpFavoriteIcon(drink.returnIconForFavorite())
                    }
                }
                is ResultWrapper.Error -> {
                    drinkDetailsBinding.constraintLayoutRootLoading.visibility = View.GONE
                    drinkDetailsBinding.constraintLayoutRootSuccess.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setUpFavoriteIcon(resourceDrawable: Int) {
        drinkDetailsBinding.imageViewFavorite.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                resourceDrawable,
                null
            )
        )
    }
}