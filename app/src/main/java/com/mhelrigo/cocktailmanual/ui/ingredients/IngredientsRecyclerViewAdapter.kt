package com.mhelrigo.cocktailmanual.ui.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.ItemIngredientBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import mhelrigo.cocktailmanual.domain.model.Ingredient

class IngredientsRecyclerViewAdapter(val onIngredientClick: OnItemClickListener<Ingredient>) :
    RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.strIngredient1 == newItem.strIngredient1
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.strIngredient1 == newItem.strIngredient1
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

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
        fun bind(ingredient: Ingredient) {
            itemIngredientBinding.textViewName.text = ingredient.strIngredient1
            Glide.with(itemIngredientBinding.root.context)
                .load(ingredient.thumbNail())
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                .into(itemIngredientBinding.imageViewThumbnail)

            itemIngredientBinding.root.setOnClickListener {
                onIngredientClick.onClick(ingredient)
            }
        }
    }
}