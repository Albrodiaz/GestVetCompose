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
        const val APPOINTMENT_COLLECTION = "appointments"
    }

    val appointments: Flow<List<AppointmentModel>> get() = callbackFlow {
        firebase.dataBase.collection(APPOINTMENT_COLLECTION).addSnapshotListener { value, error ->
            value?.let { values ->
                trySend(values.map { it.toObject(AppointmentModel::class.java) })
            }
            error?.let {
                Log.e("AppointmentService", "Error al cargar los datos: $error")
            }
        }
        awaitClose()
    }

    suspend fun addAppointment(appointmentModel: AppointmentModel) {
        val appointment = hashMapOf(
            "owner" to appointmentModel.owner,
            "pet" to appointmentModel.pet,
            "date" to appointmentModel.date,
            "hour" to appointmentModel.hour,
            "subject" to appointmentModel.subject,
            "details" to appointmentModel.details,
            "id" to appointmentModel.id
        )

        firebase.dataBase.collection(APPOINTMENT_COLLECTION).add(appointment).await()
    }

}