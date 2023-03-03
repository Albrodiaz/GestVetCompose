package com.albrodiaz.gestvet.domain

import com.albrodiaz.gestvet.data.network.AppointmentsService
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppointmentsUseCase @Inject constructor(private val appointmentsService: AppointmentsService) {
    operator fun invoke(): Flow<List<AppointmentModel>> = appointmentsService.appointments
}