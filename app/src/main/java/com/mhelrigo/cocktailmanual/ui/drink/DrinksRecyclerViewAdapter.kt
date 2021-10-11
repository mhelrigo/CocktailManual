package com.mhelrigo.cocktailmanual.ui.drink

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkBigBinding
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkRegularBinding
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkSmallBinding
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.BaseViewHolder
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.BigViewHolder
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.RegularViewHolder
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.SmallViewHolder
import com.mhelrigo.cocktailmanual.model.DrinkModel
import com.mhelrigo.cocktailmanual.model.DrinkModel.Factory.VIEW_HOLDER_BIG
import com.mhelrigo.cocktailmanual.model.DrinkModel.Factory.VIEW_HOLDER_REGULAR
import com.mhelrigo.cocktailmanual.model.DrinkModel.Factory.VIEW_HOLDER_SMALL
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

class DrinksRecyclerViewAdapter(
) : RecyclerView.Adapter<BaseViewHolder>() {
    private val _drinks = mutableListOf<DrinkModel>()

    private val _expandItem = MutableSharedFlow<DrinkModel>()
    val expandItem: SharedFlow<DrinkModel> get() = _expandItem

    private val _toggleFavorite = MutableSharedFlow<DrinkModel>()
    val toggleFavorite: SharedFlow<DrinkModel> get() = _toggleFavorite

    fun submitList(p0: List<DrinkModel>) {
        _drinks.clear()
        _drinks.addAll(p0)
        notifyDataSetChanged()
    }

    fun toggleFavoriteOfADrink(position: Int, drink: DrinkModel) {
        _drinks.find { a -> a.idDrink == drink.idDrink }?.isFavourite = drink.isFavourite
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return when (viewType) {
            VIEW_HOLDER_SMALL -> {
                SmallViewHolder(
                    ItemDrinkSmallBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_HOLDER_REGULAR -> {
                RegularViewHolder(
                    ItemDrinkRegularBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_HOLDER_BIG -> {
                BigViewHolder(
                    ItemDrinkBigBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int
    ) {
        holder.bind(
            _drinks[position],
            _toggleFavorite,
            _expandItem
        )
    }

    override fun getItemCount() = _drinks.size

    override fun getItemViewType(position: Int): Int {
        return _drinks[position].returnViewHolderType()
    }
}