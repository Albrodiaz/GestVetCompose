package com.albrodiaz.gestvet.ui.features.home.viewmodels.clients

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.data.network.ClientService.Companion.CLIENTS_TAG
import com.albrodiaz.gestvet.domain.AddClientUseCase
import com.albrodiaz.gestvet.domain.GetClientsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientDetailsViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getClientsUseCase: GetClientsUseCase,
    private val addClientUseCase: AddClientUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val selectedClient: Flow<ClientsModel> =
        state.getStateFlow("id", 0L).flatMapLatest { id ->
            getClientsUseCase.invoke(id).map {
                it.toObject(ClientsModel::class.java)
            }
        }

    init {
        setData()
    }

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name
    fun setName(nameText: String) {
        _name.value = nameText
    }

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> get() = _lastName
    fun setLastName(lastnameText: String) {
        _lastName.value = lastnameText
    }

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> get() = _address
    fun setAddress(addressText: String) {
        _address.value = addressText
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email
    fun setEmail(emailText: String) {
        _email.value = emailText
    }

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> get() = _phone
    fun setPhone(phoneText: String) {
        _phone.value = phoneText
    }

    private val _clientId = MutableStateFlow("")
    val clientId: StateFlow<String> get() = _clientId
    fun setClientId(clientIdText: String) {
        _clientId.value = clientIdText
    }

    private val _clientSeniority = MutableStateFlow(0L)
    val clientSeniority: StateFlow<Long> get() = _clientSeniority

    private val _isEditActive = MutableStateFlow(false)
    val isEditActive: StateFlow<Boolean> get() = _isEditActive
    fun enableEdit(value: Boolean) {
        _isEditActive.value = value
    }

    private fun setData() {
        viewModelScope.launch {
            selectedClient.collect {
                _name.value = it.name ?: ""
                _lastName.value = it.lastname ?: ""
                _address.value = it.address ?: ""
                _email.value = it.email ?: ""
                _phone.value = it.phoneNumber ?: ""
                _clientSeniority.value = it.id
                _clientId.value = it.clientId ?: ""
            }
        }
    }

    fun saveChanges() {
        try {
            viewModelScope.launch {
                val client = ClientsModel(
                    name = name.value,
                    lastname = lastName.value,
                    address = address.value,
                    email = email.value,
                    phoneNumber =  phone.value,
                    clientId = clientId.value,
                    id = state.get<Long>("id")!!
                )
                addClientUseCase.invoke(client)
            }
        } catch (error: Throwable) {
            Log.e(CLIENTS_TAG, "Error al guardar los datos: ${error.message}")
        }
    }
}