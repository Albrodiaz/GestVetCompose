package com.albrodiaz.gestvet.data.network

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationService @Inject constructor(private val firebaseClient: FirebaseClient) {

    val hasUser: Boolean get() = firebaseClient.auth.currentUser != null

    suspend fun login(email: String, password: String, onResult: (Throwable?) -> Unit) {
        firebaseClient.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }.await()
    }

    suspend fun createAccount(email: String, password: String): AuthResult? {
        return firebaseClient.auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun logOut() {
        firebaseClient.auth.signOut()
    }

    suspend fun deleteAccount() = runCatching {
        firebaseClient.auth.currentUser?.delete()?.await()
    }.isSuccess

    suspend fun recoverPassword(email: String) = runCatching {
        firebaseClient.auth.sendPasswordResetEmail(email).await()
    }.isSuccess
}