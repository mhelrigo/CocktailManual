package com.mhelrigo.cocktailmanual.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentSearchBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.drink.DrinkNavigator
import com.mhelrigo.cocktailmanual.ui.drink.DrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import com.mhelrigo.cocktailmanual.ui.model.Drink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), DrinkNavigator {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var drinksRecyclerViewAdapter: DrinksRecyclerViewAdapter

    private lateinit var searchedDrinkTemp: CharSequence

    override fun inflateLayout(inflater: LayoutInflater): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextWatcherForSearching()
        setUpRecyclerView()
        handleDrinks()

        if (isTablet!!) {
            refreshDrinksWhenItemToggled()
        }
    }

    private fun searchForDrinkByName(p0: CharSequence) {
        drinksViewModel.searchForDrinkByName(p0)
    }

    private fun setUpTextWatcherForSearching() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    searchedDrinkTemp = it
                    searchForDrinkByName(it)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun setUpRecyclerView() {
        drinksRecyclerViewAdapter = DrinksRecyclerViewAdapter(object : OnItemClickListener<Drink> {
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
                drinksViewModel.setMealToBeSearched(item.idDrink!!)
                navigateToDrinkDetail(
                    R.id.action_searchFragment_to_drinkDetailsFragment,
                    null,
                    findNavController(),
                    isTablet!!
                )
            }
        })

        binding.recyclerViewSearch.apply {
            adapter = drinksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleDrinks() {
        drinksViewModel.drinkSearchedByName.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding.imageViewSearchDrinkLoading
            )
            when (it) {
                is ResultWrapper.Success -> {
                    binding.recyclerViewSearch.visibility = View.VISIBLE
                    binding.imageViewSearchDrinkLoading.visibility = View.GONE
                    binding.textViewErrorSearch.visibility = View.GONE
                    drinksRecyclerViewAdapter.differ.submitList(it.value)
                }
                is ResultWrapper.Error -> {
                    binding.recyclerViewSearch.visibility = View.GONE
                    binding.imageViewSearchDrinkLoading.visibility = View.GONE
                    binding.textViewErrorSearch.visibility = View.VISIBLE
                }
                is ResultWrapper.Loading -> {
                    binding.recyclerViewSearch.visibility = View.GONE
                    binding.imageViewSearchDrinkLoading.visibility = View.VISIBLE
                    binding.textViewErrorSearch.visibility = View.GONE
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
                searchForDrinkByName(searchedDrinkTemp)
            }
        }
    }
}