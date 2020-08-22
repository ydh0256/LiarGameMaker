package com.duckkite.android.liargamemaker.data.event

data class ErrorEvent(
    val errorEventType: ErrorEventType,
    val errorEventViewType: ErrorEventViewType,
    val stringMessage: String? = null,
    val resourceMessage: Int? = null,
    val customError: String? = null
)

enum class ErrorEventType {
    NORMAL,
    CLOSE,
    CUSTOM
}

enum class ErrorEventViewType {
    POPUP,
    TOAST,
    SNACK_BAR
}
