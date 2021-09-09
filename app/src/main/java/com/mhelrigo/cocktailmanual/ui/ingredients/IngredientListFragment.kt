package com.mhelrigo.cocktailmanual.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentIngredientListBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.navigateWithBundle
import com.mhelrigo.cocktailmanual.ui.setUpDeviceBackNavigation
import com.mhelrigo.commons.NAME
import mhelrigo.cocktailmanual.domain.model.Ingredient
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

class IngredientListFragment : Fragment() {
    private lateinit var ingredientListBinding: FragmentIngredientListBinding
    private lateinit var ingredientRecyclerViewAdapter: IngredientsRecyclerViewAdapter

    private val ingredientListFragment: IngredientsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ingredientListBinding = FragmentIngredientListBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return ingredientListBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpDeviceBackNavigation()
    }

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
        ingredientListBinding.recyclerViewIngredients.apply {
            adapter = ingredientRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun requestForIngredients() {
        ingredientListFragment.requestForIngredients()
    }

    private fun handleIngredients() {
        ingredientListFragment.ingredients.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    ingredientListBinding.recyclerViewIngredients.visibility = View.VISIBLE
                    ingredientListBinding.textViewError.visibility = View.GONE
                    ingredientListBinding.shimmerFrameLayout.visibility = View.GONE
                    ingredientListBinding.shimmerFrameLayout.stopShimmer()
                    ingredientRecyclerViewAdapter.differ.submitList(it.value.drinks)
                }
                is ResultWrapper.Loading -> {
                    ingredientListBinding.shimmerFrameLayout.visibility = View.VISIBLE
                    ingredientListBinding.textViewError.visibility = View.GONE
                    ingredientListBinding.recyclerViewIngredients.visibility = View.GONE
                    ingredientListBinding.shimmerFrameLayout.startShimmer()
                }
                is ResultWrapper.Error -> {
                    ingredientListBinding.textViewError.visibility = View.VISIBLE
                    ingredientListBinding.recyclerViewIngredients.visibility = View.GONE
                    ingredientListBinding.shimmerFrameLayout.visibility = View.GONE
                    ingredientListBinding.shimmerFrameLayout.stopShimmer()
                }
            }
        })
    }
}