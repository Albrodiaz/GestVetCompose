package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppointmentsService @Inject constructor(firebase: FirebaseClient) {

    private val currentUser = firebase.auth.currentUser?.email

    companion object {
        const val APPOINTMENT_TAG = "Appointments"
    }

    private val appointmentReference = firebase.dataBase.collection("$currentUser/management/appointments")
    fun appointments() = callbackFlow {
            val data = appointmentReference.orderBy("dateInMillis")
                .addSnapshotListener { values, error ->
                    error?.let {
                        Log.e(APPOINTMENT_TAG, "Error al cargar los datos: ${it.message}")
                    }
                    values?.let { value ->
                        trySend(value)
                    }
                }
            awaitClose { data.remove() }
        }

    fun getAppointmentById(id: Long): Flow<QueryDocumentSnapshot> = callbackFlow {
        val data = appointmentReference.whereEqualTo("id", id)
            .addSnapshotListener { values, error ->
                error?.let {
                    Log.e(APPOINTMENT_TAG, "No hemos encontrado la cita: ${it.message}")
                }
                values?.let { _ ->
                    values.map {
                        trySend(it)
                    }
                }
            }
        awaitClose { data.remove() }
    }


    suspend fun addAppointment(appointmentModel: AppointmentModel) {
        appointmentReference.document("${appointmentModel.id}")
            .set(appointmentModel).await()
    }

    suspend fun deleteAppointment(id: Long) {
        appointmentReference.document("$id")
            .delete()
            .addOnSuccessListener { Log.i(APPOINTMENT_TAG, "Item $id borrado") }
            .addOnCanceledListener {
                Log.i(
                    APPOINTMENT_TAG,
                    "Fallo al borrar el item $id"
                )
            }
            .await()
    }

}