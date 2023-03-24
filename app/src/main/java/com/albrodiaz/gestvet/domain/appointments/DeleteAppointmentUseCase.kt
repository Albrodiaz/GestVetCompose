package com.albrodiaz.gestvet.domain.appointments

import com.albrodiaz.gestvet.data.network.AppointmentsService
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(private val appointmentsService: AppointmentsService) {
    suspend operator fun invoke(appointmentModel: AppointmentModel) = appointmentsService.deleteAppointment(appointmentModel)
}