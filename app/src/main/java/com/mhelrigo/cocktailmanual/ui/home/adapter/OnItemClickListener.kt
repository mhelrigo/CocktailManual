package com.mhelrigo.cocktailmanual.ui.home.adapter

interface OnItemClickListener<T> {
    fun onClick(item: Pair<Int, T>)
}