package com.albrodiaz.gestvet.ui.features.login.model

data class User(
    val id: Long? = System.currentTimeMillis(),
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
)
