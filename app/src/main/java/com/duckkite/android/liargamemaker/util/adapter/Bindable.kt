package com.duckkite.android.liargamemaker.util.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.duckkite.android.liargamemaker.data.model.Entity

interface Bindable<M> {
    fun bindModel(model: M)
}

abstract class BindableViewHolder<E: Entity>(view: View): RecyclerView.ViewHolder(view), Bindable<E>