package com.albrodiaz.gestvet.ui.features.home.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.core.extensions.combine
import com.albrodiaz.gestvet.core.extensions.dateToMillis
import com.albrodiaz.gestvet.core.extensions.hourToMillis
import com.albrodiaz.gestvet.domain.AddAppointmentUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAppointmentViewModel @Inject constructor(
    private val addAppointmentUseCase: AddAppointmentUseCase
) : ViewModel() {

    private val _isAddedSuccess = MutableLiveData(true)
    val isAddedSuccess: LiveData<Boolean?> get() = _isAddedSuccess

    private val _showDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean> get() = _showDatePicker
    fun setShowDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

    private var _ownerText = MutableLiveData("")
    val ownerText: LiveData<String> get() = _ownerText
    fun setOwner(owner: String) {
        _ownerText.value = owner
    }

    private val _petText = MutableLiveData("")
    val petText: LiveData<String> get() = _petText
    fun setPet(pet: String) {
        _petText.value = pet
    }

    private val _dateText = MutableLiveData("")
    val dateText: LiveData<String> get() = _dateText
    fun setDate(date: String) {
        _dateText.value = date
    }

    private val _hourText = MutableLiveData("")
    val hourText: LiveData<String> get() = _hourText
    fun setHour(hour: String) {
        _hourText.value = hour
    }

    private val _subjectText = MutableLiveData("")
    val subjectText: LiveData<String> get() = _subjectText
    fun setSubject(subject: String) {
        _subjectText.value = subject
    }

    private val _detailsText = MutableLiveData("")
    val detailsText: LiveData<String> get() = _detailsText
    fun setDetails(details: String) {
        _detailsText.value = details
    }

    var isButtonEnabled: LiveData<Boolean> = ownerText.combine(
        petText,
        dateText,
        hourText,
        subjectText
    ) { owner, pet, date, hour, subject ->
        return@combine owner.isNotEmpty() && pet.isNotEmpty() && date.isNotEmpty() && hour.isNotEmpty() && subject.isNotEmpty()
    }

    fun addAppointment() {
        _isAddedSuccess.value = true
        viewModelScope.launch {
            try {
                addAppointmentUseCase.invoke(
                    AppointmentModel(
                        _ownerText.value,
                        _petText.value,
                        _dateText.value,
                        _hourText.value,
                        _subjectText.value,
                        _detailsText.value,
                        _dateText.value!!.dateToMillis() + _hourText.value!!.hourToMillis() /*TODO: Usar otro id y usar objetos Date para enviar a Firebase*/
                    )
                )
            } catch (e: Throwable) {
                Log.e("AddAppointmentViewModel", "Error al añadir la cita: $e")
                _isAddedSuccess.value = false
            }
        }
    }
}