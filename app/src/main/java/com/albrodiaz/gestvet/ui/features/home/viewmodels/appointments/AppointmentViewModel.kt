package com.albrodiaz.gestvet.ui.features.home.viewmodels.appointments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.domain.DeleteAppointmentUseCase
import com.albrodiaz.gestvet.domain.GetAppointmentsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    getAppointmentsUseCase: GetAppointmentsUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase
) :
    ViewModel() {

    private val _appointments: Flow<List<AppointmentModel>> = getAppointmentsUseCase.invoke().map {values ->
        values.toObjects(AppointmentModel::class.java)
    }
    val appointments: Flow<List<AppointmentModel>> get() = _appointments

    private val _visibleDeleteDialog = MutableLiveData<Boolean>()
    val visibleDeleteDialog: LiveData<Boolean> get() = _visibleDeleteDialog
    fun showDeleteDialog(enabled: Boolean) {
        _visibleDeleteDialog.value = enabled
    }

    private val _selectedAppointment: MutableLiveData<AppointmentModel> = MutableLiveData(null)
    val selectedAppointment: LiveData<AppointmentModel> get() = _selectedAppointment
    fun setSelectedAppointment(appointment: AppointmentModel) {
        _selectedAppointment.value = appointment
    }

    fun deleteAppointment(appointmentModel: AppointmentModel) {
        viewModelScope.launch {
            deleteAppointmentUseCase.invoke(appointmentModel)
        }
    }
}