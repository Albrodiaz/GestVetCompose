package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.core.extensions.getDocumentFlow
import com.albrodiaz.gestvet.core.extensions.getFlow
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class AppointmentsService @Inject constructor(firebase: FirebaseClient) {

    private val currentUser = firebase.auth.currentUser?.email

    companion object {
        const val APPOINTMENT_TAG = "Appointments"
    }

    private val appointmentReference = firebase.dataBase.collection("$currentUser/management/appointments")
    fun appointments() = appointmentReference.whereGreaterThan("apptDate", Date()).orderBy("apptDate").getFlow()

    fun getAllAppointments() = appointmentReference.orderBy("apptDate").getFlow()

    fun getAppointmentById(id: Long) = appointmentReference
        .whereEqualTo("id", id).getDocumentFlow()


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