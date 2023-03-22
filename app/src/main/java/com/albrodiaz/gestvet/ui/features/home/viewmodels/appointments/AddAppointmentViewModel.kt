package com.albrodiaz.gestvet.ui.features.home.viewmodels.appointments

import android.util.Log
import androidx.lifecycle.*
import com.albrodiaz.gestvet.core.extensions.combine
import com.albrodiaz.gestvet.core.extensions.dateToMillis
import com.albrodiaz.gestvet.core.extensions.hourToMillis
import com.albrodiaz.gestvet.domain.AddAppointmentUseCase
import com.albrodiaz.gestvet.domain.GetAppointmentsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAppointmentViewModel @Inject constructor(
    private val addAppointmentUseCase: AddAppointmentUseCase,
    getAppointmentsUseCase: GetAppointmentsUseCase,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _appointmentId = state.getLiveData("id", 0L)

    private val dateList = MutableLiveData<List<Long>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val selectedAppt: Flow<AppointmentModel> =
        state.getStateFlow("id", 0L).flatMapLatest { id ->
            getAppointmentsUseCase.invoke(id).map { it.toObject(AppointmentModel::class.java) }
        }

    init {
        setData()
        viewModelScope.launch {
            getAppointmentsUseCase.invoke().collect { appointments ->
                dateList.value = appointments.toObjects(AppointmentModel::class.java).map {
                    it.dateInMillis
                }
            }
        }
    }

    private val _isAddedSuccess = MutableLiveData(true)
    val isAddedSuccess: LiveData<Boolean?> get() = _isAddedSuccess

    private val _showDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean> get() = _showDatePicker
    fun setShowDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

    private val _showTimePicker = MutableLiveData(false)
    val showTimePicker: LiveData<Boolean> get() = _showTimePicker
    fun setShowTimePicker(show: Boolean) {
        _showTimePicker.value = show
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

    private val _dateText = MutableLiveData<String>()
    val dateText: LiveData<String> get() = _dateText
    fun setDate(date: String) {
        _dateText.value = date
    }

    private val _hourText = MutableLiveData<String>()
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

    private fun setData() {
        viewModelScope.launch {
            selectedAppt.collect {
                setOwner(it.owner ?: "")
                setPet(it.pet ?: "")
                setDate(it.date ?: "")
                setHour(it.hour ?: "")
                setSubject(it.subject ?: "")
                setDetails(it.details ?: "")
            }
        }
    }

    private val _isDateUnavailable: MutableLiveData<Boolean> =
        dateText.combine(hourText) { date, hour ->
            return@combine dateList.value?.contains(hour.hourToMillis() + date.dateToMillis())
                ?: false
        } as MutableLiveData<Boolean>

    val isDateUnavailable: LiveData<Boolean> get() = _isDateUnavailable

    fun cancelDateSnackbar() {
        _isDateUnavailable.value = false
    }

    var isButtonEnabled: LiveData<Boolean> = ownerText.combine(
        petText,
        dateText,
        hourText,
        subjectText,
        isDateUnavailable
    ) { owner, pet, date, hour, subject, error ->
        return@combine owner.isNotEmpty() && pet.isNotEmpty() && date.isNotEmpty() && hour.isNotEmpty() && subject.isNotEmpty() && !error
    }

    fun saveAppointment() {
        _isAddedSuccess.value = true
        try {
            val appointment = AppointmentModel(
                owner = _ownerText.value,
                pet = _petText.value,
                date = _dateText.value,
                hour = _hourText.value,
                dateInMillis = _dateText.value!!.dateToMillis() + _hourText.value!!.hourToMillis(),
                subject = _subjectText.value,
                details = _detailsText.value,
                id = if (state.get<Long>("id") == 0L) System.currentTimeMillis() else _appointmentId.value
            )
            viewModelScope.launch {
                addAppointmentUseCase.invoke(appointment)
            }
        } catch (e: Throwable) {
            Log.e("AddAppointmentViewModel", "Error al añadir la cita: $e")
            _isAddedSuccess.value = false
        }
    }
}