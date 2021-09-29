package com.mhelrigo.cocktailmanual.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentIngredientListBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.base.BaseFragment
import com.mhelrigo.cocktailmanual.ui.navigateWithBundle
import com.mhelrigo.commons.NAME
import mhelrigo.cocktailmanual.domain.model.Ingredient
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

class IngredientListFragment : BaseFragment<FragmentIngredientListBinding>() {
    private lateinit var ingredientRecyclerViewAdapter: IngredientsRecyclerViewAdapter

    private val ingredientListFragment: IngredientsViewModel by activityViewModels()

    override fun inflateLayout(inflater: LayoutInflater): FragmentIngredientListBinding =
        FragmentIngredientListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        handleIngredients()
        requestForIngredients()
    }

    private fun setUpRecyclerView() {
        ingredientRecyclerViewAdapter =
            IngredientsRecyclerViewAdapter(object : OnItemClickListener<Ingredient> {
                override fun onClick(item: Ingredient) {
                    navigateWithBundle(Bundle().apply {
                        putString(NAME, item.strIngredient1)
                    }, R.id.action_ingredientListFragment_to_ingredientDetailsFragment)
                }
            })
        binding.recyclerViewIngredients.apply {
            adapter = ingredientRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun requestForIngredients() {
        ingredientListFragment.requestForIngredients()
    }

    private fun handleIngredients() {
        ingredientListFragment.ingredients.observe(viewLifecycleOwner, {
            processLoadingState(
                it.equals(ResultWrapper.Loading),
                binding.imageViewIngredientsLoading
            )
            when (it) {
                is ResultWrapper.Success -> {
                    binding.recyclerViewIngredients.visibility = View.VISIBLE
                    binding.textViewError.visibility = View.GONE
                    binding.imageViewIngredientsLoading.visibility = View.GONE
                    ingredientRecyclerViewAdapter.differ.submitList(it.value.drinks)
                }
                is ResultWrapper.Loading -> {
                    binding.imageViewIngredientsLoading.visibility = View.VISIBLE
                    binding.textViewError.visibility = View.GONE
                    binding.recyclerViewIngredients.visibility = View.GONE
                }
                is ResultWrapper.Error -> {
                    binding.textViewError.visibility = View.VISIBLE
                    binding.recyclerViewIngredients.visibility = View.GONE
                    binding.imageViewIngredientsLoading.visibility = View.GONE
                }
            }
        })
    }
}