package com.albrodiaz.gestvet.ui.features.home.models

data class ConsultationModel(
    val id: Long? = System.currentTimeMillis(),
    val petId: Long? = null,
    val date: String? = null,
    val description: String? = null
)
