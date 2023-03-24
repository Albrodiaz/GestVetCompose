package com.albrodiaz.gestvet.ui.features.home.views.navigation

sealed class Routes(val route: String) {
    object Appointment: Routes("appointmentscreen")
    object Search: Routes("searchscreen")
    object Client: Routes("clientscreen")
    object AddAppointment: Routes("addappointmentscreen?id={id}") {
        fun createRoute(id: Long) = "addappointmentscreen?id=$id"
    }
    object AddClient: Routes("addclientscreen")
    object ClientDetails: Routes("clientdetailscreen/{id}") {
        fun createRoute(id: Long) = "clientdetailscreen/$id"
    }
    object AddPet: Routes("addpetscreen/{ownerId}") {
        fun createRoute(ownerId: Long) = "addpetscreen/$ownerId"
    }
}