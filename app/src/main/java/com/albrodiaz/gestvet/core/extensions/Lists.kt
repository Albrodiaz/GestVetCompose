package com.albrodiaz.gestvet.core.extensions

import androidx.compose.foundation.lazy.LazyListState
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel

fun List<AppointmentModel>.searchBy(text: String): List<AppointmentModel> {
    val filteredList = this.filter {
        it.owner.lowercase().contains(text.lowercase()) ||
                it.pet.lowercase().contains(text.lowercase())
    }
    return if (text.isEmpty()) emptyList() else filteredList
}

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex < 1 || firstVisibleItemScrollOffset < 1