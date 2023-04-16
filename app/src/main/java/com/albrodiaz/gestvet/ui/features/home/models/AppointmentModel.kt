package com.albrodiaz.gestvet.ui.features.home.models

import java.util.Date

data class AppointmentModel(
    val owner: String? = null,
    val pet: String? = null,
    val apptDate: Date? = null,
    val subject: String? = null,
    val details: String? = null,
    val id: Long? = null
)
