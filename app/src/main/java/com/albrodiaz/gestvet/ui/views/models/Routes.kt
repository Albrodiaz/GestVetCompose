package com.albrodiaz.gestvet.ui.theme.views.models

sealed class Routes(val route: String) {
    object Appointment: Routes("appointmentscreen")
    object Search: Routes("searchscreen")
    object Client: Routes("clientscreen")
    object Add: Routes("addscreen")
}