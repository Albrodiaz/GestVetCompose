package com.albrodiaz.gestvet.domain.appointments

import com.albrodiaz.gestvet.data.network.AppointmentsService
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppointmentByIdUseCase @Inject constructor(
    private val appointmentsService: AppointmentsService
) {
    operator fun invoke(id: Long): Flow<QueryDocumentSnapshot> = appointmentsService.getAppointmentById(id)
}