package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppointmentsService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val APPOINTMENT_TAG = "Appointments"
        const val APPOINTMENTS_PATH = "users/alrodiaz15@gmail.com/appointments"
    }

    val appointments
        get() = callbackFlow {
            val data = firebase.dataBase.collection(APPOINTMENTS_PATH)
                .addSnapshotListener { values, error ->
                    error?.let {
                        Log.e(APPOINTMENT_TAG, "Error al cargar los datos: $error")
                    }
                    values?.let { value ->
                        trySend(value)
                    }
                }
            awaitClose { data.remove() }
        }

    suspend fun addAppointment(appointmentModel: AppointmentModel) {
        firebase.dataBase.collection(APPOINTMENTS_PATH).document("${appointmentModel.id}")
            .set(appointmentModel).await()
    }

    suspend fun deleteAppointment(appointmentModel: AppointmentModel) {
        firebase.dataBase.collection(APPOINTMENTS_PATH).document("${appointmentModel.id}")
            .delete()
            .addOnSuccessListener { Log.i(APPOINTMENT_TAG, "Item ${appointmentModel.id} borrado") }
            .addOnCanceledListener {
                Log.i(
                    APPOINTMENT_TAG,
                    "Fallo al borrar el item ${appointmentModel.id}"
                )
            }
            .await()
    }

}