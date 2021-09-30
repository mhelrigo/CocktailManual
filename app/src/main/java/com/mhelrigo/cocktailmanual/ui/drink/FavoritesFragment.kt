package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentFavoritesBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.commons.ID
import dagger.hilt.android.AndroidEntryPoint
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

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
        favoritesAdapter = DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
            override fun onClick(item: Drink) {
                when (val result =
                    drinksViewModel.toggleFavoriteOfADrink(item)) {
                    is ResultWrapper.Success -> {
                        drinksViewModel.setMealToBeSearched(item.idDrink!!)
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
                    R.id.action_favoritesFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )
            }
        })

        binding.recyclerViewFavorites.apply {
            adapter = favoritesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleFavorites() {
        drinksViewModel.favoriteDrinks.observe(viewLifecycleOwner, {
            it?.let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        favoritesAdapter.differ.submitList(result.value)
                        if (result.value.isNotEmpty()) {
                            binding.recyclerViewFavorites.visibility = View.VISIBLE
                            binding.textViewFavoritesError.visibility = View.GONE
                        } else {
                            binding.textViewFavoritesError.visibility =
                                View.VISIBLE
                        }
                    }
                    is ResultWrapper.Error -> {
                        binding.recyclerViewFavorites.visibility = View.GONE
                        binding.textViewFavoritesError.visibility = View.VISIBLE
                    }
                    is ResultWrapper.Loading -> {
                        binding.recyclerViewFavorites.visibility = View.GONE
                        binding.textViewFavoritesError.visibility = View.GONE
                    }
                }
            }
        })
    }
}