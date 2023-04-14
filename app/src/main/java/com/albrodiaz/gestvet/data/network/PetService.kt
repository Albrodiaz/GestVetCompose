package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.core.extensions.getDocumentFlow
import com.albrodiaz.gestvet.core.extensions.getFlow
import com.albrodiaz.gestvet.ui.features.home.models.ConsultationModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PetService @Inject constructor(firebaseClient: FirebaseClient) {


    private val currentUser = firebaseClient.auth.currentUser?.email

    companion object {
        const val PETS_TAG = "PetsService"
    }

    private val petReference = firebaseClient.dataBase.collection("$currentUser/management/pets")
    private val consultationRef =
        firebaseClient.dataBase.collection("$currentUser/management/consultations")

    fun pets() = petReference.getFlow()

    fun getPetsByOwner(ownerId: Long) = petReference.whereEqualTo("owner", ownerId).getFlow()

    fun getPetById(petId: Long) = petReference.whereEqualTo("id", petId).getDocumentFlow()

    fun getConsultByPet(petId: Long) = consultationRef
        .whereEqualTo("petId", petId)
        .orderBy("id", Query.Direction.DESCENDING).getFlow()

    suspend fun addPet(petModel: PetModel) {
        petReference.document("${petModel.id}")
            .set(petModel).await()
    }

    suspend fun deletePet(id: Long) {
        petReference.document("$id")
            .delete().await()
        consultationRef.whereEqualTo("petId", id).addSnapshotListener { value, error ->
            error?.let {
                Log.e(PETS_TAG, "Error al recuperar las consultas: ${it.message}")
            }
            value?.let { values ->
                values.forEach {
                    consultationRef.document(it.id).delete()
                }
            }
        }
    }

    suspend fun addConsultation(consultation: ConsultationModel) {
        consultationRef.document("${consultation.id}")
            .set(consultation).await()
    }

    suspend fun deleteConsultation(id:Long) {
        consultationRef.document("$id")
            .delete().await()
    }


}