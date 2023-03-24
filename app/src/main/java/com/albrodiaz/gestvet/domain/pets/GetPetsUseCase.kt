package com.albrodiaz.gestvet.domain.pets

import com.albrodiaz.gestvet.data.network.PetService
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(private val petService: PetService) {

    operator fun invoke(ownerId: Long) = petService.getPets(ownerId)

}