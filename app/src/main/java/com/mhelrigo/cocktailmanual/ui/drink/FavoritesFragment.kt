package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentFavoritesBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.commons.ID
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

class FavoritesFragment : Fragment() {
    private lateinit var fragmentFavoritesBinding: FragmentFavoritesBinding

    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var favoritesAdapter: DrinksRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return fragmentFavoritesBinding.root
    }

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
                        // It would be a lot easier to just request the whole list again.
                        drinksViewModel.requestForFavoriteDrinks()
                    }
                    is ResultWrapper.Error -> {
                        Timber.e("Something went wrong sport...")
                    }
                }
            }
        }, object : OnItemClickListener<Drink> {
            override fun onClick(item: Drink) {
                expandDrinkDetails(item)
            }
        })

        fragmentFavoritesBinding.recyclerViewFavorites.apply {
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
                            fragmentFavoritesBinding.recyclerViewFavorites.visibility = View.VISIBLE
                            fragmentFavoritesBinding.textViewFavoritesError.visibility = View.GONE
                        } else {
                            fragmentFavoritesBinding.textViewFavoritesError.visibility = View.VISIBLE
                        }
                    }
                    is ResultWrapper.Error -> {
                        fragmentFavoritesBinding.recyclerViewFavorites.visibility = View.GONE
                        fragmentFavoritesBinding.textViewFavoritesError.visibility = View.VISIBLE
                    }
                    is ResultWrapper.Loading -> {
                        fragmentFavoritesBinding.recyclerViewFavorites.visibility = View.GONE
                        fragmentFavoritesBinding.textViewFavoritesError.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun expandDrinkDetails(drink: Drink) {
        val bundle = Bundle()
        bundle.putString(ID, drink.idDrink)
        findNavController().navigate(R.id.action_favoritesFragment_to_drinkDetailsFragment, bundle)
    }
}