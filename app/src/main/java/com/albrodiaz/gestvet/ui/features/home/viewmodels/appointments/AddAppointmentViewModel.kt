package com.albrodiaz.gestvet.ui.features.home.viewmodels.appointments

import androidx.lifecycle.*
import com.albrodiaz.gestvet.core.extensions.dateToMillis
import com.albrodiaz.gestvet.core.extensions.hourToMillis
import com.albrodiaz.gestvet.core.extensions.showLeftZero
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.domain.appointments.AddAppointmentUseCase
import com.albrodiaz.gestvet.domain.appointments.GetAppointmentByIdUseCase
import com.albrodiaz.gestvet.domain.appointments.GetAppointmentsUseCase
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddAppointmentViewModel @Inject constructor(
    private val addAppointmentUseCase: AddAppointmentUseCase,
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    state: SavedStateHandle
) : ViewModel() {

    private val appointmentId = state.getStateFlow("id", 0L)

    private val dateList = mutableListOf<Date>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val selectedAppt: Flow<AppointmentModel> =
        appointmentId.flatMapLatest { id ->
            getAppointmentByIdUseCase.invoke(id).map { it.toObject(AppointmentModel::class.java) }
        }

    init {
        setData()
        viewModelScope.launch {
            getAppointmentsUseCase.invoke().collect { appointments ->
                appointments.toObjects(AppointmentModel::class.java).map {
                    dateList.add(it.apptDate!!)
                }
            }
        }
    }

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> get() = _showDatePicker
    fun setShowDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> get() = _showTimePicker
    fun setShowTimePicker(show: Boolean) {
        _showTimePicker.value = show
    }

    private var _ownerText = MutableStateFlow("")
    val ownerText: StateFlow<String> get() = _ownerText
    fun setOwner(owner: String) {
        _ownerText.value = owner
    }

    private val _petText = MutableStateFlow("")
    val petText: StateFlow<String> get() = _petText
    fun setPet(pet: String) {
        _petText.value = pet
    }

    private val _dateText = MutableStateFlow("")
    val dateText: StateFlow<String> get() = _dateText
    fun setDate(date: String) {
        _dateText.value = date
    }

    private val _hourText = MutableStateFlow("")
    val hourText: StateFlow<String> get() = _hourText
    fun setHour(hour: String) {
        _hourText.value = hour
    }

    private val _subjectText = MutableStateFlow("")
    val subjectText: StateFlow<String> get() = _subjectText
    fun setSubject(subject: String) {
        _subjectText.value = subject
    }

    private val _detailsText = MutableStateFlow("")
    val detailsText: StateFlow<String> get() = _detailsText
    fun setDetails(details: String) {
        _detailsText.value = details
    }

    private fun setData() {
        viewModelScope.launch {
            selectedAppt.collect {
                setOwner(it.owner ?: "")
                setPet(it.pet ?: "")
                setDate(it.apptDate!!.time.toDate())
                setHour("${it.apptDate.hours}:${it.apptDate.minutes.showLeftZero()}")
                setSubject(it.subject ?: "")
                setDetails(it.details ?: "")
            }
        }
    }

    private val userPetFields = ownerText.combine(petText) { owner, pet ->
        return@combine owner.isNotEmpty() && pet.isNotEmpty()
    }

    private val dateTimeFields = dateText.combine(hourText) { date, hour ->
        return@combine date.isNotEmpty() && hour.isNotEmpty()
    }

    private val validUserData = userPetFields.combine(dateTimeFields) { user, date ->
        return@combine user && date
    }

    val isButtonEnabled = validUserData.combine(subjectText) { data, subject ->
        return@combine data && subject.isNotEmpty()
    }

    fun saveAppointment(dateUnavailable: () -> Unit, onError: () -> Unit, success: () -> Unit) {
        try {
            val appointment = AppointmentModel(
                owner = _ownerText.value,
                pet = _petText.value,
                apptDate = Date(_dateText.value.dateToMillis() + _hourText.value.hourToMillis()),
                subject = _subjectText.value,
                details = _detailsText.value,
                id = if (appointmentId.value == 0L) System.currentTimeMillis() else appointmentId.value
            )
            val isDateUnavailable =
                dateList.contains(Date(dateText.value.dateToMillis() + hourText.value.hourToMillis()))

            viewModelScope.launch {
                if (isDateUnavailable) {
                    dateUnavailable()
                } else {
                    addAppointmentUseCase.invoke(appointment)
                    success()
                }
            }
        } catch (e: Throwable) {
            onError()
        }
    }
}