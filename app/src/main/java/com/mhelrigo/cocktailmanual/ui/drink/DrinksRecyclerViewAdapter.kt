package com.mhelrigo.cocktailmanual.ui.drink

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkBigBinding
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkRegularBinding
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkSmallBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.BaseViewHolder
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.BigViewHolder
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.RegularViewHolder
import com.mhelrigo.cocktailmanual.ui.drink.viewholders.SmallViewHolder
import com.mhelrigo.cocktailmanual.ui.model.BIG
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.mhelrigo.cocktailmanual.ui.model.REGULAR
import com.mhelrigo.cocktailmanual.ui.model.SMALL

class DrinksRecyclerViewAdapter(
    var onFavoritesToggled: OnItemClickListener<Drink>,
    var onItemClicked: OnItemClickListener<Drink>,
) : RecyclerView.Adapter<BaseViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.idDrink?.toInt() == newItem.idDrink?.toInt()
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.strDrink == newItem.strDrink && oldItem.idDrink?.toInt() == newItem.idDrink?.toInt() && oldItem.isFavourite == newItem.isFavourite
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    fun toggleFavoriteOfADrink(position: Int, drink: Drink) {
        differ.currentList.find { a -> a.idDrink == drink.idDrink }?.isFavourite = drink.isFavourite
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return when (viewType) {
            SMALL -> {
                SmallViewHolder(
                    ItemDrinkSmallBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            REGULAR -> {
                RegularViewHolder(
                    ItemDrinkRegularBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            BIG -> {
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
            onFavoritesToggled,
            onItemClicked
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].returnViewHolderType()
    }
}