package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val CLIENTS_TAG = "clients"
        const val CLIENTS_PATH = "alrodiaz15@gmail.com/management/clients"
    }

    val clients
        get() = callbackFlow {
            val data = firebase.dataBase.collection(CLIENTS_PATH)
                .orderBy("lastname", Query.Direction.ASCENDING)
                .addSnapshotListener { values, error ->
                    error?.let {
                        Log.e(CLIENTS_TAG, "Error al cargar los datos: ${it.message}")
                    }

                    values?.let { value ->
                        trySend(value)
                    }
                }
            awaitClose { data.remove() }
        }

    fun getClientById(id: Long) = callbackFlow {
        val data = firebase.dataBase.collection(CLIENTS_PATH).whereEqualTo("id", id)
            .addSnapshotListener { values, error ->
                error?.let {
                    Log.e(CLIENTS_TAG, "Error al recuperar el cliente: ${it.message}")
                }
                values?.let { client ->
                    client.map {
                        trySend(it)
                    }
                }
            }
        awaitClose { data.remove() }
    }

    suspend fun addClient(clientsModel: ClientsModel) {
        firebase.dataBase.collection(CLIENTS_PATH).document("${clientsModel.id}")
            .set(clientsModel).await()
    }

}