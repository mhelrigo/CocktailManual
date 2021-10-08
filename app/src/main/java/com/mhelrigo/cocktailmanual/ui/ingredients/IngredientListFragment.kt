package com.mhelrigo.cocktailmanual.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentIngredientListBinding
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.ui.commons.base.BaseFragment
import com.mhelrigo.commons.NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * This will show all list of ingredients, each item can be expanded for more detailed information.
 * */
@AndroidEntryPoint
class IngredientListFragment : BaseFragment<FragmentIngredientListBinding>() {
    private lateinit var ingredientRecyclerViewAdapter: IngredientsRecyclerViewAdapter

    private val ingredientsViewModel: IngredientsViewModel by activityViewModels()

    override fun inflateLayout(inflater: LayoutInflater): FragmentIngredientListBinding =
        FragmentIngredientListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        handleIngredients()

        requestData()
    }

    private fun setUpRecyclerView() {
        ingredientRecyclerViewAdapter = IngredientsRecyclerViewAdapter()

        ingredientRecyclerViewAdapter.expandItem
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { ingredient ->
                navigateWithBundle(Bundle().apply {
                    putString(NAME, ingredient.strIngredient1)
                }, R.id.action_ingredientListFragment_to_ingredientDetailsFragment)
            }.launchIn(lifecycleScope)

        binding.recyclerViewIngredients.apply {
            adapter = ingredientRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun requestForIngredients() {
        ingredientsViewModel.requestForIngredients()
    }

    private fun handleIngredients() {
        ingredientsViewModel.ingredients
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                processLoadingState(
                    state is ViewStateWrapper.Loading,
                    binding.imageViewIngredientsLoading
                )

                when (state) {
                    is ViewStateWrapper.Loading -> {
                        binding.imageViewIngredientsLoading.visibility = View.VISIBLE
                        binding.textViewError.visibility = View.GONE
                        binding.recyclerViewIngredients.visibility = View.GONE
                    }
                    is ViewStateWrapper.Error -> {
                        binding.textViewError.visibility = View.VISIBLE
                        binding.recyclerViewIngredients.visibility = View.GONE
                        binding.imageViewIngredientsLoading.visibility = View.GONE
                    }
                    is ViewStateWrapper.Success -> {
                        binding.recyclerViewIngredients.visibility = View.VISIBLE
                        binding.textViewError.visibility = View.GONE
                        binding.imageViewIngredientsLoading.visibility = View.GONE
                        ingredientRecyclerViewAdapter.differ.submitList(state.data.ingredients)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun requestData() {
        super.requestData()
        if (ingredientsViewModel.ingredients.value.noResultYet()) {
            requestForIngredients()
        }
    }
}