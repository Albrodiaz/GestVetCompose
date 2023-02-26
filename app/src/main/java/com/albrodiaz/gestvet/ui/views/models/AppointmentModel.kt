package com.albrodiaz.gestvet.ui.views.models

data class AppointmentModel(
    val owner: String,
    val pet: String,
    val date: String,
    val hour: String,
    val subject: String,
    val details: String,
    val id: Int
)
