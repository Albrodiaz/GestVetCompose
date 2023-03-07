package com.albrodiaz.gestvet.domain

import com.albrodiaz.gestvet.data.network.AppointmentsService
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import javax.inject.Inject

class AddAppointmentUseCase @Inject constructor(private val appointmentsService: AppointmentsService) {
    suspend operator fun invoke(appointment: AppointmentModel) = appointmentsService.addAppointment(appointment)
}