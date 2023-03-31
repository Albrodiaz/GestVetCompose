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
}

/* private val authentication = firebaseClient.auth

    val currentUserId: String
        get() = authentication.currentUser?.uid.orEmpty()

    val hasUser: Boolean
        get() = authentication.currentUser != null

    val currentUser
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let { *//*User con datos o vacíos*//* })
            }
            authentication.addAuthStateListener(listener)
            awaitClose { authentication.removeAuthStateListener(listener) }
        }*/