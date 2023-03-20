package com.albrodiaz.gestvet.ui.features.home.views.mainscreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.albrodiaz.gestvet.ui.features.home.models.Routes
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AddAppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.clients.AddClientScreen
import com.albrodiaz.gestvet.ui.features.home.views.clients.ClientDetailScreen
import com.albrodiaz.gestvet.ui.features.home.views.clients.ClientScreen
import com.albrodiaz.gestvet.ui.features.home.views.searchscreen.SearchScreen

@Composable
fun MainNavController(
    navigationController: NavController,
    appointmentViewModel: AppointmentViewModel,
    isDateAvailable: (String) -> Unit
) {
    NavHost(
        navController = navigationController as NavHostController,
        startDestination = Routes.Appointment.route
    ) {
        composable(Routes.Appointment.route) {
            AppointmentScreen(appointmentViewModel, navigationController)
        }
        composable(Routes.Client.route) {
            ClientScreen(
                clientViewModel = hiltViewModel(),
                navigationController
            )
        }
        composable(Routes.Search.route) { SearchScreen(appointmentViewModel) }
        composable(
            Routes.AddAppointment.route,
            arguments = listOf(navArgument("id") { defaultValue = 1L })
        ) {
            AddAppointmentScreen(
                addAppointmentViewModel = hiltViewModel(),
                navigationController = navigationController,
                isDateAvailable = { available -> isDateAvailable(available) },
                appointmentId = it.arguments?.getLong("id")
            )
        }
        composable(Routes.AddClient.route) {
            AddClientScreen(
                navigationController = navigationController,
                addClientViewModel = hiltViewModel()
            )
        }
        composable(Routes.ClientDetails.route) {
            ClientDetailScreen(navigationController)
        }
    }
}