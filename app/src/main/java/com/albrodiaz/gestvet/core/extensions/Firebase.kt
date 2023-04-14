package com.albrodiaz.gestvet.core.extensions

import android.util.Log
import com.albrodiaz.gestvet.data.network.AppointmentsService
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun Query.getFlow() = callbackFlow {
    val data = this@getFlow.addSnapshotListener { value, error ->
        error?.let {
            Log.e(
                AppointmentsService.APPOINTMENT_TAG,
                "Error al recuperar los datos: ${it.message}"
            )
        }
        value?.let {
            trySend(it)
        }
    }
    awaitClose { data.remove() }
}

fun Query.getDocumentFlow() = callbackFlow {
    val data = this@getDocumentFlow.addSnapshotListener { value, error ->
        error?.let {
            Log.e(
                AppointmentsService.APPOINTMENT_TAG,
                "Error al recuperar los datos: ${it.message}"
            )
        }
        value?.let { querysnapshot ->
            querysnapshot.map {
                trySend(it)
            }
        }
    }
    awaitClose { data.remove() }
}