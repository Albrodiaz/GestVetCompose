package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppointmentsService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val APPOINTMENT_TAG = "Appointments"
        const val APPOINTMENTS_PATH = "users/alrodiaz15@gmail.com/appointments"
    }

    val appointments: Flow<List<AppointmentModel>>
        get() = callbackFlow {
            firebase.dataBase.collection(APPOINTMENTS_PATH)
                .addSnapshotListener { value, error ->
                    value?.let { values ->
                        trySend(values.map { it.toObject(AppointmentModel::class.java) })
                    }
                    error?.let {
                        Log.e(APPOINTMENT_TAG, "Error al cargar los datos: $error")
                    }
                }
            awaitClose()
        }

    suspend fun addAppointment(appointmentModel: AppointmentModel) {
        firebase.dataBase.collection(APPOINTMENTS_PATH).document("${appointmentModel.id}").set(
            hashMapOf(
                "owner" to appointmentModel.owner,
                "pet" to appointmentModel.pet,
                "date" to appointmentModel.date,
                "hour" to appointmentModel.hour,
                "subject" to appointmentModel.subject,
                "details" to appointmentModel.details,
                "id" to appointmentModel.id
            )
        ).await()
    }

    suspend fun deleteAppointment(appointmentModel: AppointmentModel) {
        firebase.dataBase.collection(APPOINTMENTS_PATH).document("${appointmentModel.id}")
            .delete()
            .addOnSuccessListener { Log.i(APPOINTMENT_TAG, "Item ${appointmentModel.id} borrado") }
            .addOnCanceledListener { Log.i(APPOINTMENT_TAG, "Fallo al borrar el item ${appointmentModel.id}") }
            .await()
    }

}