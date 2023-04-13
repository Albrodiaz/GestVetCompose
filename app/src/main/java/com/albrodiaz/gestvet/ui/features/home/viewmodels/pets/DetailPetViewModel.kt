package com.albrodiaz.gestvet.ui.features.home.viewmodels.pets

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.core.extensions.dateToMillis
import com.albrodiaz.gestvet.core.extensions.hourToMillis
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.data.network.PetService.Companion.PETS_TAG
import com.albrodiaz.gestvet.domain.pets.*
import com.albrodiaz.gestvet.ui.features.home.models.ConsultationModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailPetViewModel @Inject constructor(
    private val getPetByIdUseCase: GetPetByIdUseCase,
    private val addPetUseCase: AddPetUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val addConsultationUseCase: AddConsultationUseCase,
    private val getConsultationsUseCase: GetConsultationsUseCase,
    private val deleteConsultationUseCase: DeleteConsultationUseCase,
    state: SavedStateHandle
) : ViewModel() {

    private val petId = state.getStateFlow("petId", 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedPet = state.getStateFlow("petId", 0L).flatMapLatest { id ->
        getPetByIdUseCase.invoke(id).map {
            it.toObject(PetModel::class.java)
        }
    }

    init {
        setData()
        setConsultations()
    }

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog
    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }

    private val _showConsultationDialog = MutableStateFlow(false)
    val showConsultationDialog: StateFlow<Boolean> get() = _showConsultationDialog
    fun setConsultationDialog(value: Boolean) {
        _showConsultationDialog.value = value
    }

    private val _editEnabled = MutableStateFlow(false)
    val editEnabled: StateFlow<Boolean> get() = _editEnabled
    fun setEdit(value: Boolean) {
        _editEnabled.value = value
    }

    private var ownerId: Long? = null

    private val _consultationDate = MutableStateFlow(System.currentTimeMillis().toDate())
    val consultationDate: StateFlow<String> get() = _consultationDate
    fun setConsultDate(date: String) {
        _consultationDate.value = date
    }

    private val _consultationHour = MutableStateFlow("")
    val consultationHour: StateFlow<String> get() = _consultationHour
    fun setConsultationHour(hour: String) {
        _consultationHour.value = hour
    }

    private val _consultationDetail = MutableStateFlow("")
    val consultationDetail: StateFlow<String> get() = _consultationDetail
    fun setConsultDetail(detail: String) {
        _consultationDetail.value = detail
    }

    private val _consultations = MutableStateFlow<List<ConsultationModel>>(emptyList())
    val consultations: StateFlow<List<ConsultationModel>> get() = _consultations

    private val _petName = MutableStateFlow("")
    val petName: StateFlow<String> get() = _petName
    fun setPetName(name: String) {
        _petName.value = name
    }

    private val _petBirth = MutableStateFlow("")
    val petBirth: StateFlow<String> get() = _petBirth
    fun setPetBirth(date: String) {
        _petBirth.value = date
    }

    private val _petBreed = MutableStateFlow("")
    val petBreed: StateFlow<String> get() = _petBreed
    fun setPetBreed(breed: String) {
        _petBreed.value = breed
    }

    private val _petColor = MutableStateFlow("")
    val petColor: StateFlow<String> get() = _petColor
    fun setPetColor(color: String) {
        _petColor.value = color
    }

    private val _petChip = MutableStateFlow("")
    val petChip: StateFlow<String> get() = _petChip
    fun setPetChip(chip: String) {
        _petChip.value = chip
    }

    private val _petPassport = MutableStateFlow("")
    val petPassport: StateFlow<String> get() = _petPassport
    fun setPetPassport(passport: String) {
        _petPassport.value = passport
    }

    private val _petNeutered = MutableStateFlow(false)
    val petNeutered: StateFlow<Boolean> get() = _petNeutered
    fun setPetNeutered(neutered: Boolean) {
        _petNeutered.value = neutered
    }

    private fun setData() {
        viewModelScope.launch {
            selectedPet.collect {
                ownerId = it.owner
                _petName.value = it.name ?: ""
                _petBirth.value = it.birthDate ?: ""
                _petBreed.value = it.breed ?: ""
                _petColor.value = it.color ?: ""
                _petChip.value = "${it.chipNumber}"
                _petPassport.value = "${it.passportNumeber}"
                _petNeutered.value = it.neutered ?: false
            }
        }
    }

    private fun setConsultations() {
        viewModelScope.launch {
            getConsultationsUseCase.invoke(petId.value).collect {
                _consultations.value = it.toObjects(ConsultationModel::class.java)
            }
        }
    }

    fun updateData() {
        val petModel = PetModel(
            owner = ownerId,
            id = petId.value,
            name = petName.value,
            birthDate = petBirth.value,
            breed = petBreed.value,
            color = petColor.value,
            chipNumber = petChip.value,
            passportNumeber = petPassport.value,
            neutered = petNeutered.value
        )
        try {
            viewModelScope.launch {
                addPetUseCase.invoke(petModel)
            }
        } catch (error: Throwable) {
            Log.e(PETS_TAG, "Error al actualizar los datos: ${error.message}")
        }
    }

    fun deletePet() {
        viewModelScope.launch {
            deletePetUseCase.invoke(petId.value)
        }
    }

    fun addConsultation() {
        val consultationId =
            consultationDate.value.dateToMillis() + consultationHour.value.hourToMillis()
        val consultation = ConsultationModel(
            id = consultationId,
            petId = petId.value,
            date = consultationDate.value,
            description = consultationDetail.value
        )
        viewModelScope.launch {
            try {
                addConsultationUseCase.invoke(consultation)
            } catch (error: Throwable) {
                Log.e(PETS_TAG, "Error al guardar la consulta: ${error.message}")
            }
        }
    }

    fun deleteConsultation(id: Long) {
        viewModelScope.launch {
            deleteConsultationUseCase.invoke(id)
        }
    }
}