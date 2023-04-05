package com.albrodiaz.gestvet.domain.appointments

import com.albrodiaz.gestvet.data.network.AppointmentsService
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(private val appointmentsService: AppointmentsService) {
    suspend operator fun invoke(id: Long) = appointmentsService.deleteAppointment(id)
}