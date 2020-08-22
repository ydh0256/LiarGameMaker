package com.duckkite.android.liargamemaker.util.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.duckkite.android.liargamemaker.data.model.Entity
import com.duckkite.android.liargamemaker.ui.main.adapter.GameListAdapter

abstract class BindableAdapter<VH : BindableViewHolder<E>, E: Entity> :
    RecyclerView.Adapter<VH>() {
    protected val entityList: MutableList<E> = mutableListOf()

    override fun getItemCount() = entityList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindModel(entityList[position])
    }

    fun setItems(items: List<E>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(entityList, items))
        entityList.clear()
        entityList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DiffCallback(
        private val oldData: List<E>,
        private val newData: List<E>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldData[oldItemPosition] == newData[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldData[oldItemPosition] == newData[newItemPosition]
    }
}