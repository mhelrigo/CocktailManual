package com.mhelrigo.cocktailmanual.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.cocktailmanual.databinding.FragmentSearchBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.drink.DrinksRecyclerViewAdapter
import com.mhelrigo.cocktailmanual.ui.drink.DrinksViewModel
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.cocktailmanual.ui.navigateWithBundle
import com.mhelrigo.cocktailmanual.ui.setUpDeviceBackNavigation
import com.mhelrigo.commons.ID
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import timber.log.Timber

class SearchFragment : Fragment() {
    private lateinit var searchBinding: FragmentSearchBinding
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private lateinit var drinksRecyclerViewAdapter: DrinksRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchBinding = FragmentSearchBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return searchBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpDeviceBackNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextWatcherForSearching()
        setUpRecyclerView()
        handleDrinks()
    }

    private fun setUpTextWatcherForSearching() {
        searchBinding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    drinksViewModel.searchForDrinkByName(it)
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
                navigateWithBundle(Bundle().apply {
                    putString(ID, item.idDrink)
                }, R.id.action_searchFragment_to_drinkDetailsFragment)
            }
        })

        searchBinding.recyclerViewSearch.apply {
            adapter = drinksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleDrinks() {
        drinksViewModel.drinkSearchedByName.observe(viewLifecycleOwner, {
            when (it) {
                is ResultWrapper.Success -> {
                    searchBinding.recyclerViewSearch.visibility = View.VISIBLE
                    searchBinding.shimmerFrameLayoutSearch.visibility = View.GONE
                    searchBinding.textViewErrorSearch.visibility = View.GONE
                    drinksRecyclerViewAdapter.differ.submitList(it.value)
                }
                is ResultWrapper.Error -> {
                    searchBinding.recyclerViewSearch.visibility = View.GONE
                    searchBinding.shimmerFrameLayoutSearch.visibility = View.GONE
                    searchBinding.textViewErrorSearch.visibility = View.VISIBLE
                }
                is ResultWrapper.Loading -> {
                    searchBinding.recyclerViewSearch.visibility = View.GONE
                    searchBinding.shimmerFrameLayoutSearch.visibility = View.VISIBLE
                    searchBinding.textViewErrorSearch.visibility = View.GONE
                }
            }
        })
    }
}