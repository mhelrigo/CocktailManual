package com.mhelrigo.cocktailmanual.ui.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.ItemIngredientBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.entity.IngredientEntity

class IngredientsRecyclerViewAdapter() :
    RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<IngredientEntity>() {
        override fun areItemsTheSame(oldItem: IngredientEntity, newItem: IngredientEntity): Boolean {
            return oldItem.strIngredient1 == newItem.strIngredient1
        }

        override fun areContentsTheSame(oldItem: IngredientEntity, newItem: IngredientEntity): Boolean {
            return oldItem.strIngredient1 == newItem.strIngredient1
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private val _expandItem = MutableSharedFlow<IngredientEntity>()
    val expandItem : SharedFlow<IngredientEntity> get() = _expandItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(var itemIngredientBinding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(itemIngredientBinding.root) {
        fun bind(ingredient: IngredientEntity) {
            itemIngredientBinding.textViewName.text = ingredient.strIngredient1
            Glide.with(itemIngredientBinding.root.context)
                .load(ingredient.thumbNail())
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                .into(itemIngredientBinding.imageViewThumbnail)

            itemIngredientBinding.root.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _expandItem.emit(ingredient)
                }
            }
        }
    }
}