package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.ConsultationModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PetService @Inject constructor(firebaseClient: FirebaseClient) {


    private val currentUser = firebaseClient.auth.currentUser?.email
    companion object {
        const val PETS_TAG = "PetsService"
    }

    private val petReference = firebaseClient.dataBase.collection("$currentUser/management/pets")
    private val consultationRef = firebaseClient.dataBase.collection("$currentUser/management/consultations")

    fun pets() = callbackFlow {
        val data = petReference.addSnapshotListener { value, error ->
            error?.let {
                Log.e(PETS_TAG, "Error al cargar las mascotas: ${it.message}")
            }
            value?.let {
                trySend(it)
            }
        }
        awaitClose { data.remove() }
    }

    fun getPetsByOwner(ownerId: Long) = callbackFlow {
        val data = petReference.whereEqualTo("owner", ownerId)
            .addSnapshotListener { values, error ->
                error?.let {
                    Log.e(PETS_TAG, "Error al recuperar las mascotas ${it.message}")
                }
                values?.let { snapshot ->
                    trySend(snapshot)
                }
            }
        awaitClose { data.remove() }
    }

    fun getPetById(petId: Long) = callbackFlow {
        val data = petReference.whereEqualTo("id", petId)
            .addSnapshotListener { values, error ->
                error?.let {
                    Log.e(PETS_TAG, "Error al recuperar la mascota ${it.message}")
                }
                values?.let { snapshot ->
                    snapshot.map {
                        trySend(it)
                    }
                }
            }
        awaitClose { data.remove() }
    }

    suspend fun addPet(petModel: PetModel) {
        petReference.document("${petModel.id}")
            .set(petModel).await()
    }

    suspend fun deletePet(id: Long) {
        petReference.document("$id")
            .delete().await()
    }

    suspend fun addConsultation(consultation: ConsultationModel) {
        consultationRef.document("${consultation.id}")
            .set(consultation).await()
    }


}