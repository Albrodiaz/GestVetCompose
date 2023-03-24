package com.albrodiaz.gestvet.ui.features.home.models

data class PetModel(
    val id: Long? = System.currentTimeMillis(),
    val owner: Long? = null,
    val name: String? = null,
    val birthDate: String? = null,
    val breed: String? = null,
    val chipNumber: Long? = null,
    val passportNumeber: Long? = null,
    val neutered: Boolean? = null
)
