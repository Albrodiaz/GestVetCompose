package com.albrodiaz.gestvet.ui.features.home.viewmodels.pets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _editEnabled = MutableStateFlow(false)
    val editEnabled: StateFlow<Boolean> get() = _editEnabled
    fun setEdit(value: Boolean) {
        _editEnabled.value = value
    }

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
    private val _petNeutered = MutableStateFlow("")
    val petNeutered: StateFlow<String> get() = _petNeutered
    fun setPetNeutered(neutered: String) {
        _petNeutered.value = neutered
    }

    fun setData() {
        viewModelScope.launch {
            selectedPet.collectLatest {
                _petName.value = it.name?:""
            }
        }
    }
}