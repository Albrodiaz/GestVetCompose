package com.albrodiaz.gestvet.ui.features.home.models

data class ClientsModel(
    val name: String? = null,
    val lastname: String? = null,
    val id: Long = System.currentTimeMillis(),
    val address: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val clientId: String? = null
)
