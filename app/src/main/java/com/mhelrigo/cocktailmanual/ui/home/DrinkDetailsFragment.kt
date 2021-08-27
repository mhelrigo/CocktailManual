package com.mhelrigo.cocktailmanual.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.FragmentDrinkDetailsBinding
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

/**
 * This fragment will expanded information about a Drink.
 * It also have a capability of toggling favorites of said Drink.
 */
class DrinkDetailsFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

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

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateBack()
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drinkDetailsBinding.imageViewFavorite.setOnClickListener {
            when (val result =
                homeViewModel.toggleFavoriteOfADrink(homeViewModel.expandedDrinkDetails.value!!)) {
                is ResultWrapper.Success -> {
                    homeViewModel.expandDrinkDetails(result.value)
                }
                is ResultWrapper.Error -> {
                    Timber.e("Something went wrong sport...")
                }
            }
        }

        drinkDetailsBinding.imageViewBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun onResume() {
        super.onResume()
        handleDrinkDetails()
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun handleDrinkDetails() {
        homeViewModel.expandedDrinkDetails.observe(viewLifecycleOwner, {
            Timber.e("Drink $it")
            drinkDetailsBinding.textViewName.text = it.strDrink
            drinkDetailsBinding.textViewShortDesc.text =
                "${it.strCategory} | ${it.strAlcoholic} | ${it.strGlass}"
            drinkDetailsBinding.textViewMeasurements.text = it.returnMeasurements()
            drinkDetailsBinding.textViewIngredients.text =
                it.returnIngredients()
            drinkDetailsBinding.textViewInstruction.text = it.strInstructions

            Glide.with(requireContext()).load(it.strDrinkThumb).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(drinkDetailsBinding.imageViewThumbnail)

            setUpFavoriteIcon(it.returnIconForFavorite())
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