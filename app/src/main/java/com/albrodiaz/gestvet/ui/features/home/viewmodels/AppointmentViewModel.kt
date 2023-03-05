package com.albrodiaz.gestvet.ui.features.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.albrodiaz.gestvet.domain.GetAppointmentsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(getAppointmentsUseCase: GetAppointmentsUseCase): ViewModel() {

    private val _appointments: Flow<List<AppointmentModel>> = getAppointmentsUseCase.invoke()
    val appointments: Flow<List<AppointmentModel>> get() = _appointments

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> get() = _showDialog
    fun enableDialog(enabled: Boolean) {
        _showDialog.value = enabled
    }





}