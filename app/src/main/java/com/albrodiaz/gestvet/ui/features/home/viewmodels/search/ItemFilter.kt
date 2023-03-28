package com.albrodiaz.gestvet.ui.features.home.viewmodels.search

import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel

fun List<Any>.searchBy(text: String): List<Any> {
    val filteredList = this.filter {
        when (it) {
            is AppointmentModel -> {
                it.owner!!.lowercase().contains(text.lowercase()) ||
                        it.pet!!.lowercase().contains(text.lowercase())
            }
            is ClientsModel -> {
                it.name!!.lowercase().contains(text.lowercase()) ||
                        it.lastname!!.lowercase().contains(text.lowercase())
            }
            is PetModel -> {
                it.name!!.lowercase().contains(text.lowercase()) ||
                        it.breed!!.lowercase().contains(text.lowercase())
            }
            else -> false
        }

    }
    return if (text.isEmpty()) emptyList() else filteredList
}