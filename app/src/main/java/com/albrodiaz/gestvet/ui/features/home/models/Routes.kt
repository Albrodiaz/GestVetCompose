package com.albrodiaz.gestvet.ui.features.home.models

sealed class Routes(val route: String) {
    object Appointment: Routes("appointmentscreen")
    object Search: Routes("searchscreen")
    object Client: Routes("clientscreen")
    object AddAppointment: Routes("addappointmentscreen")
}