package com.duckkite.android.liargamemaker.util.adapter

import android.view.ViewGroup
import com.duckkite.android.liargamemaker.data.model.Entity

interface BindableViewHolderFactory<E: Entity> {
    fun createFromViewType(parent: ViewGroup, viewType: Int): BindableViewHolder<E>
}