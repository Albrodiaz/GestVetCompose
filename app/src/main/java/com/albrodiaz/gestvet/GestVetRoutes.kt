package com.albrodiaz.gestvet

sealed class GestVetRoutes(val route: String) {
    object Appointment: GestVetRoutes("appointmentscreen")
    object Search: GestVetRoutes("searchscreen")
    object Client: GestVetRoutes("clientscreen")
    object AddAppointment: GestVetRoutes("addappointmentscreen?id={id}") {
        fun createRoute(id: Long) = "addappointmentscreen?id=$id"
    }
    object AddClient: GestVetRoutes("addclientscreen")
    object ClientDetails: GestVetRoutes("clientdetailscreen/{id}") {
        fun createRoute(id: Long) = "clientdetailscreen/$id"
    }
    object AddPet: GestVetRoutes("addpetscreen/{ownerId}") {
        fun createRoute(ownerId: Long) = "addpetscreen/$ownerId"
    }
    object PetDetails: GestVetRoutes("petdetailscreen/{petId}") {
        fun createRoute(petId: Long) = "petdetailscreen/$petId"
    }
    object LoginScreen: GestVetRoutes("loginscreen")
    object RegisterScreen: GestVetRoutes("registerscreen")
    object MainScreen: GestVetRoutes("mainscreen")
}