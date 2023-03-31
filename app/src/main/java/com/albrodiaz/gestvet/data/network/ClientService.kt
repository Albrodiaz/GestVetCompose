package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientService @Inject constructor(firebase: FirebaseClient) {

    private val currentUser = firebase.auth.currentUser?.email
    companion object {
        const val CLIENTS_TAG = "clients"
    }

    private val clientReference = firebase.dataBase.collection("$currentUser/management/clients")

    fun clients() = callbackFlow {
            val data = clientReference
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
        val data = clientReference.whereEqualTo("id", id)
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
        clientReference.document("${clientsModel.id}")
            .set(clientsModel).await()
    }

    suspend fun deleteClient(id: Long) {
        clientReference.document("$id")
            .delete()
            .addOnSuccessListener {
                Log.i(CLIENTS_TAG, "Cita guardada con éxito")
            }
            .addOnCanceledListener {
                Log.e(CLIENTS_TAG, "Error al borrar la cita")
            }
            .await()
    }

}