package com.albrodiaz.gestvet.ui.features.home.models

data class AppointmentModel(
    val owner: String? = null,
    val pet: String? = null,
    val date: String? = null,
    val hour: String? = null,
    val dateInMillis: Long = System.currentTimeMillis(),
    val subject: String? = null,
    val details: String? = null,
    val id: Long? = null
)
