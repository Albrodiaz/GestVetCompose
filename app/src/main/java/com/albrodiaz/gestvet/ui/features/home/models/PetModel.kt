package com.albrodiaz.gestvet.ui.features.home.models

data class PetModel(
    val id: Long? = System.currentTimeMillis(),
    val owner: Long? = null,
    val name: String? = null,
    val birthDate: String? = null,
    val breed: String? = null,
    val color: String? = null,
    val chipNumber: String? = null,
    val passportNumeber: String? = null,
    val neutered: Boolean? = null
)
