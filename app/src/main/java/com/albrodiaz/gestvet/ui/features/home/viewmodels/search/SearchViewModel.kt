package com.albrodiaz.gestvet.ui.features.home.viewmodels.search

import androidx.lifecycle.ViewModel
import com.albrodiaz.gestvet.domain.appointments.GetAppointmentsUseCase
import com.albrodiaz.gestvet.domain.clients.GetClientsUseCase
import com.albrodiaz.gestvet.domain.pets.GetPetsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getAppointmentsUseCase: GetAppointmentsUseCase,
    getClientsUseCase: GetClientsUseCase,
    getPetsUseCase: GetPetsUseCase
): ViewModel() {

    private val _userText = MutableStateFlow("")
    val userText: StateFlow<String> get() = _userText
    fun setUserText(text: String) {
        _userText.value = text
    }

    val appointments = getAppointmentsUseCase.invoke().map { it.toObjects(AppointmentModel::class.java) }
    val clients = getClientsUseCase.invoke().map { it.toObjects(ClientsModel::class.java) }
    val pets = getPetsUseCase.invoke().map { it.toObjects(PetModel::class.java) }
}