package com.mhelrigo.cocktailmanual.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentIngredientDetailsBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.drink.DrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.cocktailmanual.ui.navigateWithBundle
import com.mhelrigo.cocktailmanual.ui.setUpDeviceBackNavigation
import com.mhelrigo.commons.ID
import com.mhelrigo.commons.NAME
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

class IngredientDetailsFragment : Fragment() {
    private lateinit var ingredientDetailsBinding: FragmentIngredientDetailsBinding

    private val ingredientViewModel: IngredientsViewModel by activityViewModels()
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var drinksRecyclerViewAdapter: DrinksRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ingredientDetailsBinding = FragmentIngredientDetailsBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return ingredientDetailsBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpDeviceBackNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        requestForIngredient(arguments?.getString(NAME)!!)
        handleIngredient()
        handleDrinksFilteredByIngredient()
    }

    private fun setUpRecyclerView() {
        drinksRecyclerViewAdapter =
            DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
                override fun onClick(item: Drink) {
                    when (val result =
                        drinksViewModel.toggleFavoriteOfADrink(item)) {
                        is ResultWrapper.Success -> {
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
                    navigateWithBundle(Bundle().apply {
                        putString(ID, item.idDrink)
                    }, R.id.action_ingredientDetailsFragment_to_drinkDetailsFragment)
                }
            })
        ingredientDetailsBinding.recyclerViewDrinks.apply {
            adapter = drinksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun requestForIngredient(p0: String) {
        ingredientViewModel.requestForIngredient(p0)
    }

    private fun handleIngredient() {
        ingredientViewModel.ingredient.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    ingredientDetailsBinding.textViewError.visibility = View.GONE
                    ingredientDetailsBinding.shimmerFrameLayout.visibility = View.GONE
                    ingredientDetailsBinding.shimmerFrameLayout.stopShimmer()
                    ingredientDetailsBinding.textViewName.visibility = View.VISIBLE
                    ingredientDetailsBinding.textViewDescription.visibility = View.VISIBLE
                    ingredientDetailsBinding.recyclerViewDrinks.visibility = View.VISIBLE
                    ingredientDetailsBinding.imageViewThumbnail.visibility = View.VISIBLE
                    ingredientDetailsBinding.textViewName.text = it.value.strIngredient
                    ingredientDetailsBinding.textViewDescription.text = it.value.strDescription
                    Glide.with(requireContext()).load(it.value.thumbNail())
                        .into(ingredientDetailsBinding.imageViewThumbnail)

                    drinksViewModel.filterDrinksByIngredient(it.value.strIngredient)
                }
                is ResultWrapper.Error -> {
                    ingredientDetailsBinding.textViewError.visibility = View.VISIBLE
                    ingredientDetailsBinding.imageViewThumbnail.visibility = View.GONE
                    ingredientDetailsBinding.textViewName.visibility = View.GONE
                    ingredientDetailsBinding.textViewDescription.visibility = View.GONE
                    ingredientDetailsBinding.recyclerViewDrinks.visibility = View.GONE
                    ingredientDetailsBinding.shimmerFrameLayout.visibility = View.GONE
                    ingredientDetailsBinding.shimmerFrameLayout.stopShimmer()
                }
                is ResultWrapper.Loading -> {
                    ingredientDetailsBinding.shimmerFrameLayout.visibility = View.VISIBLE
                    ingredientDetailsBinding.shimmerFrameLayout.startShimmer()
                    ingredientDetailsBinding.imageViewThumbnail.visibility = View.GONE
                    ingredientDetailsBinding.textViewError.visibility = View.GONE
                    ingredientDetailsBinding.textViewName.visibility = View.GONE
                    ingredientDetailsBinding.textViewDescription.visibility = View.GONE
                    ingredientDetailsBinding.recyclerViewDrinks.visibility = View.GONE
                }
            }
        })
    }

    private fun handleDrinksFilteredByIngredient() {
        drinksViewModel.drinksFilteredByIngredient.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    drinksRecyclerViewAdapter.differ.submitList(it.value)
                    ingredientDetailsBinding.recyclerViewDrinks.visibility = View.VISIBLE
                }
                is ResultWrapper.Loading -> {
                    ingredientDetailsBinding.recyclerViewDrinks.visibility = View.GONE
                }
                is ResultWrapper.Error -> {
                    ingredientDetailsBinding.recyclerViewDrinks.visibility = View.GONE
                }
            }
        })
    }
}