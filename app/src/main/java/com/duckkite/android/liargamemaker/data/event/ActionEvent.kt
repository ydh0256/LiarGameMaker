package com.duckkite.android.liargamemaker.data.event

import com.duckkite.android.liargamemaker.data.model.Entity

data class ActionEvent (
    val type: ActionEventType,
    val target: String? = null,
    val entity: Entity? = null,
    val customAction: String? = null
)

enum class ActionEventType {
    LANDING,
    FETCH,
    CUSTOM
}

