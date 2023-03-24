package com.albrodiaz.gestvet.domain.pets

import com.albrodiaz.gestvet.data.network.PetService
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import javax.inject.Inject

class AddPetUseCase @Inject constructor(private val petService: PetService) {
    suspend operator fun invoke(petModel: PetModel) = petService.addPet(petModel)
}