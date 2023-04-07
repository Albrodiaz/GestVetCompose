package com.albrodiaz.gestvet.domain.pets

import com.albrodiaz.gestvet.data.network.PetService
import com.albrodiaz.gestvet.ui.features.home.models.ConsultationModel
import javax.inject.Inject

class AddConsultationUseCase @Inject constructor(private val petService: PetService) {

    suspend operator fun invoke(consultation: ConsultationModel) =
        petService.addConsultation(consultation)

}