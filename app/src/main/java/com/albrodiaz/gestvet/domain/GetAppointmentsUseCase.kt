package com.albrodiaz.gestvet.domain

import com.albrodiaz.gestvet.data.network.AppointmentsService
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppointmentsUseCase @Inject constructor(private val appointmentsService: AppointmentsService) {
    operator fun invoke(): Flow<QuerySnapshot> = appointmentsService.appointments
}