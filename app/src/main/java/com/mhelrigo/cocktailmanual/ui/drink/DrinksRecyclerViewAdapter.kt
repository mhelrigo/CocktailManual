package com.mhelrigo.cocktailmanual.ui.drink

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
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

class DrinksRecyclerViewAdapter(
) : RecyclerView.Adapter<BaseViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<DrinkModel>() {
        override fun areItemsTheSame(oldItem: DrinkModel, newItem: DrinkModel): Boolean {
            return oldItem.idDrink?.toInt() == newItem.idDrink?.toInt()
        }

        override fun areContentsTheSame(oldItem: DrinkModel, newItem: DrinkModel): Boolean {
            return oldItem.strDrink == newItem.strDrink && oldItem.idDrink?.toInt() == newItem.idDrink?.toInt() && oldItem.isFavourite == newItem.isFavourite
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    private val _expandItem = MutableSharedFlow<DrinkModel>()
    val expandItem: SharedFlow<DrinkModel> get() = _expandItem

    private val _toggleFavorite = MutableSharedFlow<DrinkModel>()
    val toggleFavorite: SharedFlow<DrinkModel> get() = _toggleFavorite

    fun toggleFavoriteOfADrink(position: Int, drink: DrinkModel) {
        differ.currentList.find { a -> a.idDrink == drink.idDrink }?.isFavourite = drink.isFavourite
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
            differ.currentList[position],
            _toggleFavorite,
            _expandItem
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].returnViewHolderType()
    }
}