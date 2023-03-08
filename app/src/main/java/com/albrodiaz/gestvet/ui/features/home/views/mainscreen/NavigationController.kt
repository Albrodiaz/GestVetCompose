package com.albrodiaz.gestvet.ui.features.home.views.mainscreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.albrodiaz.gestvet.ui.features.home.models.Routes
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.clients.ClientScreen
import com.albrodiaz.gestvet.ui.features.home.views.searchscreen.SearchScreen

@Composable
fun NavController(navigationController: NavController, appointmentViewModel: AppointmentViewModel) {
    NavHost(navController = navigationController as NavHostController, startDestination = Routes.Appointment.route) {
        composable(Routes.Appointment.route) { AppointmentScreen(appointmentViewModel) }
        composable(Routes.Client.route) { ClientScreen() }
        composable(Routes.Search.route) { SearchScreen(appointmentViewModel) }
    }
}