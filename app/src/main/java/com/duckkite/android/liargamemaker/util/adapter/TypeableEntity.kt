package com.duckkite.android.liargamemaker.util.adapter

import com.duckkite.android.liargamemaker.data.model.Entity

interface TypeableEntity: Entity {
    fun getType(): Int
}