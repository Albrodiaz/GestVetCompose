package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.core.extensions.getDocumentFlow
import com.albrodiaz.gestvet.core.extensions.getFlow
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientService @Inject constructor(firebase: FirebaseClient) {

    private val currentUser = firebase.auth.currentUser?.email

    companion object {
        const val CLIENTS_TAG = "clients"
    }

    private val clientReference = firebase.dataBase.collection("$currentUser/management/clients")

    fun clients() = clientReference
        .orderBy("lastname", Query.Direction.ASCENDING).getFlow()

    fun getClientById(id: Long) = clientReference.whereEqualTo("id", id).getDocumentFlow()

    suspend fun addClient(clientsModel: ClientsModel) {
        clientReference.document("${clientsModel.id}")
            .set(clientsModel).await()
    }

    suspend fun deleteClient(id: Long) {
        clientReference.document("$id")
            .delete()
            .addOnSuccessListener {
                Log.i(CLIENTS_TAG, "Cita borrada con éxito")
            }
            .addOnCanceledListener {
                Log.e(CLIENTS_TAG, "Error al borrar la cita")
            }
            .await()
    }

}