package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.login.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserService @Inject constructor(private val firebaseClient: FirebaseClient) {

    companion object {
        const val USER_TAG = "users"
    }

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

    suspend fun getUserData() = callbackFlow {
        val data = firebaseClient.dataBase.collection(currentUser.orEmpty()).document("settings")
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e(USER_TAG, "Error al recuperar el usuario")
                }
                value?.let {
                    trySend(it)
                }
            }
        awaitClose { data.remove() }
    }

    /*TODO: Buscar forma de borrar datos de usuario (colección)*/

}