package com.albrodiaz.gestvet.core

import com.albrodiaz.gestvet.ui.views.models.AppointmentModel

fun List<AppointmentModel>.searchBy(text: String): List<AppointmentModel> {
    val filteredList = this.filter {
        it.owner.lowercase().contains(text.lowercase()) ||
                it.pet.lowercase().contains(text.lowercase())
    }
    return if (text.isEmpty()) emptyList() else filteredList
}