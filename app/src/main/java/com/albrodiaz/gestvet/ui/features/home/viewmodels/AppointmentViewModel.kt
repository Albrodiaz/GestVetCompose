package com.albrodiaz.gestvet.ui.features.home.viewmodels

import androidx.lifecycle.ViewModel
import com.albrodiaz.gestvet.domain.GetAppointmentsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(private val getAppointmentsUseCase: GetAppointmentsUseCase): ViewModel() {

    private var _appointments: Flow<List<AppointmentModel>> = getAppointmentsUseCase.invoke()
    val appointments: Flow<List<AppointmentModel>> get() = _appointments
}