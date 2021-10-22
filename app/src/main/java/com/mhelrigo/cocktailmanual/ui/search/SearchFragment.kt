package com.mhelrigo.cocktailmanual.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentSearchBinding
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.drink.DrinkNavigator
import com.mhelrigo.cocktailmanual.ui.drink.DrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Responsible for displaying searched list of drinks
 * */
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), DrinkNavigator {
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var drinksRecyclerViewAdapter: DrinksRecyclerViewAdapter
    private var searchedDrinkTemp: CharSequence = ""

    override fun inflateLayout(inflater: LayoutInflater): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextWatcherForSearching()
        setUpRecyclerView()
        handleDrinks()
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
        drinksRecyclerViewAdapter = DrinksRecyclerViewAdapter()

        drinksRecyclerViewAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { drink ->
                drinksViewModel.setDrinkToBeSearched(drink)
                navigateToDrinkDetail(
                    R.id.action_searchFragment_to_drinkDetailsFragment,
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
                    }.collect {}
            }
            .catch { throwable ->
                Timber.e("Something went wrong sport... ${throwable.message}")
            }
            .launchIn(lifecycleScope)

        binding.recyclerViewSearch.apply {
            adapter = drinksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleDrinks() {
        drinksViewModel.drinkSearchedByName.observe(viewLifecycleOwner, { state ->
            processLoadingState(
                state is ViewStateWrapper.Loading,
                binding.imageViewSearchDrinkLoading
            )

            when (state) {
                is ViewStateWrapper.Loading -> {
                    binding.recyclerViewSearch.visibility = View.GONE
                    binding.imageViewSearchDrinkLoading.visibility = View.VISIBLE
                    binding.textViewErrorSearch.visibility = View.GONE
                }
                is ViewStateWrapper.Error -> {
                    binding.recyclerViewSearch.visibility = View.GONE
                    binding.imageViewSearchDrinkLoading.visibility = View.GONE
                    binding.textViewErrorSearch.visibility = View.VISIBLE
                }
                is ViewStateWrapper.Success -> {
                    binding.recyclerViewSearch.visibility = View.VISIBLE
                    binding.imageViewSearchDrinkLoading.visibility = View.GONE
                    binding.textViewErrorSearch.visibility = View.GONE
                    drinksRecyclerViewAdapter.submitList(
                        state.data
                    )

                    if (state.data.isEmpty()) {
                        binding.textViewErrorSearch.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun requestData() {
        super.requestData()
        if (drinksViewModel.drinkSearchedByName.value?.noResultYet()!!) {
            searchForDrinkByName(searchedDrinkTemp)
        }
    }
}