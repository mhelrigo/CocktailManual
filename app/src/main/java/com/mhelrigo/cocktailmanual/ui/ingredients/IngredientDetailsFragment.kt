package com.mhelrigo.cocktailmanual.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentIngredientDetailsBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.drink.DrinkNavigator
import com.mhelrigo.cocktailmanual.ui.drink.DrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.commons.NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

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

        requestForIngredient(arguments?.getString(NAME)!!)
        handleIngredient()
        handleDrinksFilteredByIngredient()

        if (isTablet!!) {
            refreshDrinksWhenItemToggled()
        }
    }

    private fun setUpRecyclerView() {
        drinksRecyclerViewAdapter =
            DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        drinksViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
                            drinksViewModel.setMealToBeSearched(item.idDrink!!)
                            drinksRecyclerViewAdapter.toggleFavoriteOfADrink(
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
                        R.id.action_ingredientDetailsFragment_to_drinkDetailsFragment,
                        null,
                        findNavController(),
                        isTablet!!
                    )
                }
            })
        binding.recyclerViewDrinks.apply {
            adapter = drinksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun requestForIngredient(p0: String) {
        ingredientViewModel.requestForIngredient(p0)
    }

    private fun handleIngredient() {
        ingredientViewModel.ingredient.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding.imageViewIngredientLoading
            )
            when (it) {
                is ResultWrapper.Success -> {
                    binding.textViewError.visibility = View.GONE
                    binding.imageViewIngredientLoading.visibility = View.GONE
                    binding.textViewName.visibility = View.VISIBLE
                    binding.textViewDescription.visibility = View.VISIBLE
                    binding.recyclerViewDrinks.visibility = View.VISIBLE
                    binding.imageViewThumbnail.visibility = View.VISIBLE
                    binding.textViewName.text = it.value.strIngredient
                    binding.textViewDescription.text = it.value.strDescription
                    Glide.with(requireContext()).load(it.value.thumbNail())
                        .into(binding.imageViewThumbnail)

                    drinksViewModel.filterDrinksByIngredient(it.value.strIngredient)
                }
                is ResultWrapper.Error -> {
                    binding.textViewError.visibility = View.VISIBLE
                    binding.imageViewThumbnail.visibility = View.GONE
                    binding.textViewName.visibility = View.GONE
                    binding.textViewDescription.visibility = View.GONE
                    binding.recyclerViewDrinks.visibility = View.GONE
                    binding.imageViewIngredientLoading.visibility = View.GONE
                }
                is ResultWrapper.Loading -> {
                    binding.imageViewIngredientLoading.visibility = View.VISIBLE
                    binding.imageViewThumbnail.visibility = View.GONE
                    binding.textViewError.visibility = View.GONE
                    binding.textViewName.visibility = View.GONE
                    binding.textViewDescription.visibility = View.GONE
                    binding.recyclerViewDrinks.visibility = View.GONE
                }
            }
        })
    }

    private fun handleDrinksFilteredByIngredient() {
        drinksViewModel.drinksFilteredByIngredient.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    drinksRecyclerViewAdapter.differ.submitList(it.value)
                    binding.recyclerViewDrinks.visibility = View.VISIBLE
                }
                is ResultWrapper.Loading -> {
                    binding.recyclerViewDrinks.visibility = View.GONE
                }
                is ResultWrapper.Error -> {
                    binding.recyclerViewDrinks.visibility = View.GONE
                }
            }
        })
    }

    /**
     * Called to update the list of Meals on the left side of screen.
     * */
    private fun refreshDrinksWhenItemToggled() {
        CoroutineScope(mainCoroutine).launch {
            drinksViewModel.toggledDrink.collect {
                requestForIngredient(arguments?.getString(NAME)!!)
            }
        }
    }
}