package com.albrodiaz.gestvet.domain.clients

import com.albrodiaz.gestvet.data.network.ClientService
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientByIdUseCase @Inject constructor(
    private val clientService: ClientService
) {
    operator fun invoke(id: Long): Flow<QueryDocumentSnapshot> = clientService.getClientById(id)
}