package com.albrodiaz.gestvet.data.network

import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppointmentsService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val APPOINTMENT_COLLECTION = "appointments"
    }

    suspend fun getAllAppointments(): QuerySnapshot = firebase.dataBase.collection(APPOINTMENT_COLLECTION).get().await()

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