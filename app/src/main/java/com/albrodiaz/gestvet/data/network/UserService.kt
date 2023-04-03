package com.albrodiaz.gestvet.data.network

import com.albrodiaz.gestvet.ui.features.login.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserService @Inject constructor(private val firebaseClient: FirebaseClient) {

    private val currentUser = firebaseClient.auth.currentUser?.email

    suspend fun createUser(user: User) = runCatching {

        val registerUser = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "id" to user.id
        )

        firebaseClient.dataBase.collection("${user.email}").document("settings")
            .set(registerUser)
            .await()
    }.isSuccess

}