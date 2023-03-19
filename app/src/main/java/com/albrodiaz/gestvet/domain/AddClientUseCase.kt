package com.albrodiaz.gestvet.domain

import com.albrodiaz.gestvet.data.network.ClientService
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import javax.inject.Inject

class AddClientUseCase @Inject constructor(private val clientService: ClientService) {
    suspend operator fun invoke(clientsModel: ClientsModel) = clientService.addClient(clientsModel)
}