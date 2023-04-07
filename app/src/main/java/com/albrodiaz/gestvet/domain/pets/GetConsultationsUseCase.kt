package com.albrodiaz.gestvet.domain.pets

import com.albrodiaz.gestvet.data.network.PetService
import javax.inject.Inject

class GetConsultationsUseCase @Inject constructor(private val petService: PetService) {

    operator fun invoke(petId: Long) = petService.getConsultByPet(petId)

}