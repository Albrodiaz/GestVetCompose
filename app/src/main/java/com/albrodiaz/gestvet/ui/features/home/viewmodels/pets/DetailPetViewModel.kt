package com.albrodiaz.gestvet.ui.features.home.viewmodels.pets

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.data.network.PetService.Companion.PETS_TAG
import com.albrodiaz.gestvet.domain.pets.AddPetUseCase
import com.albrodiaz.gestvet.domain.pets.DeletePetUseCase
import com.albrodiaz.gestvet.domain.pets.GetPetByIdUseCase
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPetViewModel @Inject constructor(
    private val getPetByIdUseCase: GetPetByIdUseCase,
    private val addPetUseCase: AddPetUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    state: SavedStateHandle
): ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedPet = state.getStateFlow("petId", 0L).flatMapLatest { id ->
        getPetByIdUseCase.invoke(id).map {
            it.toObject(PetModel::class.java)
        }
    }

    init {
        setData()
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

    private var petId: Long? = null

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
            selectedPet.collectLatest {
                ownerId = it.owner
                petId = it.id
                _petName.value = it.name?:""
                _petBirth.value = it.birthDate?:""
                _petBreed.value = it.breed?:""
                _petColor.value = it.color?:""
                _petChip.value = "${it.chipNumber}"
                _petPassport.value = "${it.passportNumeber}"
                _petNeutered.value = it.neutered?:false
            }
        }
    }

    fun updateData() {
        val petModel = PetModel(
            owner = ownerId,
            id = petId,
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
            petId?.let {
                deletePetUseCase.invoke(it)
            }
        }
    }
}