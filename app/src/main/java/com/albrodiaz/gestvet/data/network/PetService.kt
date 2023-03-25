package com.albrodiaz.gestvet.data.network

import android.util.Log
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PetService @Inject constructor(private val firebaseClient: FirebaseClient) {

    companion object {
        const val PETS_PATH = "alrodiaz15@gmail.com/management/pets"
        const val PETS_TAG = "PetsService"
    }

    fun getPets(ownerId: Long) = callbackFlow {
        val data = firebaseClient.dataBase.collection(PETS_PATH).whereEqualTo("owner", ownerId)
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
        val data = firebaseClient.dataBase.collection(PETS_PATH).whereEqualTo("id", petId)
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
        firebaseClient.dataBase.collection(PETS_PATH).document("${petModel.id}")
            .set(petModel).await()
    }


}