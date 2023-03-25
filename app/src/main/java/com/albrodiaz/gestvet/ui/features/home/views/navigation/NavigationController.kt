package com.albrodiaz.gestvet.ui.features.home.views.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AddAppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.clients.*
import com.albrodiaz.gestvet.ui.features.home.views.searchscreen.SearchScreen

@Composable
fun MainNavController(
    navigationController: NavController,
    isDateAvailable: (String) -> Unit
) {
    NavHost(
        navController = navigationController as NavHostController,
        startDestination = Routes.Appointment.route
    ) {
        composable(Routes.Appointment.route) {
            AppointmentScreen {
                navigationController.navigate(Routes.AddAppointment.createRoute(it ?: 0L))
            }
        }
        composable(Routes.Client.route) {
            ClientScreen(
                clientViewModel = hiltViewModel(),
                navigateToCreate = { navigationController.navigate(Routes.AddClient.route) }
            ) {
                navigationController.navigate(Routes.ClientDetails.createRoute(it))
            }
        }
        composable(Routes.Search.route) { SearchScreen() }
        composable(
            Routes.AddAppointment.route,
            arguments = listOf(navArgument("id") { defaultValue = 1L })
        ) {
            AddAppointmentScreen(
                addAppointmentViewModel = hiltViewModel(),
                isDateAvailable = { available -> isDateAvailable(available) },
                appointmentId = it.arguments?.getLong("id"),
            ) {
                navigationController.popBackStack()
            }
        }
        composable(Routes.AddClient.route) {
            AddClientScreen(addClientViewModel = hiltViewModel()) {
                navigationController.popBackStack()
            }
        }
        composable(
            Routes.ClientDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            ClientDetailScreen(onNavigateUp = { navigationController.popBackStack() },
                navigateToDetails = { navigationController.navigate(Routes.PetDetails.createRoute(it)) }) {
                navigationController.navigate(Routes.AddPet.createRoute(it))
            }
        }
        composable(
            Routes.AddPet.route,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) {
            AddPetScreen {
                navigationController.popBackStack()
            }
        }
        composable(
            Routes.PetDetails.route,
            arguments = listOf(navArgument("petId") { type = NavType.LongType })
        ) {
            PetDetailScreen()
        }
    }
}