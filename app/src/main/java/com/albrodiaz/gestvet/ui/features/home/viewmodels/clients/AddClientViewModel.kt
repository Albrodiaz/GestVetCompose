package com.albrodiaz.gestvet.ui.features.home.viewmodels.clients

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.domain.AddClientUseCase
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClientViewModel @Inject constructor(private val addClientUseCase: AddClientUseCase) :
    ViewModel() {

    private val _clientName = MutableStateFlow("")
    val clientName: Flow<String> get() = _clientName
    fun setName(name: String) {
        _clientName.value = name
    }

    private val _clientLastName = MutableStateFlow("")
    val clientLastName: StateFlow<String> get() = _clientLastName
    fun setLastName(lastName: String) {
        _clientLastName.value = lastName
    }

    private val _clientAddress = MutableStateFlow("")
    val clientAddress: StateFlow<String> get() = _clientAddress
    fun setAddress(address: String) {
        _clientAddress.value = address
    }

    private val _clientPhone = MutableStateFlow("")
    val clientPhone: StateFlow<String> get() = _clientPhone
    fun setPhone(phone: String) {
        _clientPhone.value = phone
    }

    private val _clientEmail = MutableStateFlow("")
    val clientEmail: StateFlow<String> get() = _clientEmail
    fun setEmail(email: String) {
        _clientEmail.value = email
    }

    private val _clientId = MutableStateFlow<String?>(null)
    val clientId: StateFlow<String?> get() = _clientId
    fun setId(identification: String) {
        _clientId.value = identification
    }

    private val validUserName = clientName.combine(clientLastName) {name, lastname ->
        return@combine name.isNotEmpty() && lastname.isNotEmpty()
    }

    val isSaveEnabled = validUserName.combine(clientPhone) { validUserName, phone ->
        return@combine validUserName && phone.length == 9
    }

    fun saveClient() {
        viewModelScope.launch {
            try {
                addClientUseCase.invoke(
                    ClientsModel(
                        name = _clientName.value,
                        lastname = _clientLastName.value,
                        address = _clientAddress.value,
                        phoneNumber = _clientPhone.value,
                        email = _clientEmail.value,
                        clientId = if (_clientId.value.isNullOrEmpty()) "Sin especificar" else _clientId.value
                    )
                )
            } catch (e: Throwable) {
                Log.e("AddClientViewModel", "Error al guardar los datos: ${e.message}")
            }
        }
    }

}