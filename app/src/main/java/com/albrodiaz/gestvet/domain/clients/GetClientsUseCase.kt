package com.albrodiaz.gestvet.domain.clients

import com.albrodiaz.gestvet.data.network.ClientService
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(private val clientService: ClientService) {
    operator fun invoke(): Flow<QuerySnapshot> = clientService.clients

}