package com.albrodiaz.gestvet.ui.features.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.albrodiaz.gestvet.GestVetRoutes
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AddAppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AppointmentScreen
import com.albrodiaz.gestvet.ui.features.home.views.clients.*
import com.albrodiaz.gestvet.ui.features.home.views.searchscreen.SearchScreen
import com.albrodiaz.gestvet.ui.features.home.views.settingscreen.SettingScreen
import com.albrodiaz.gestvet.ui.features.login.view.HomeScreen

@Composable
fun MainNavController(
    navigationController: NavController,
    isDateAvailable: (String) -> Unit
) {
    NavHost(
        navController = navigationController as NavHostController,
        startDestination = GestVetRoutes.Appointment.route
    ) {
        composable(GestVetRoutes.Appointment.route) {
            AppointmentScreen {
                navigationController.navigate(GestVetRoutes.AddAppointment.createRoute(it ?: 0L))
            }
        }
        composable(GestVetRoutes.Client.route) {
            ClientScreen(
                clientViewModel = hiltViewModel(),
                navigateToCreate = { navigationController.navigate(GestVetRoutes.AddClient.route) }
            ) {
                navigationController.navigate(GestVetRoutes.ClientDetails.createRoute(it))
            }
        }
        composable(GestVetRoutes.Search.route) { _ ->
            SearchScreen(
                navigateApptDetails = {
                    navigationController.navigate(GestVetRoutes.AddAppointment.createRoute(it))
                },
                navigateClientDetail = {
                    navigationController.navigate(GestVetRoutes.ClientDetails.createRoute(it))
                },
                navigatePetDetail = {
                    navigationController.navigate(GestVetRoutes.PetDetails.createRoute(it))
                }
            )
        }
        composable(
            GestVetRoutes.AddAppointment.route,
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
        composable(GestVetRoutes.AddClient.route) {
            AddClientScreen(addClientViewModel = hiltViewModel()) {
                navigationController.popBackStack()
            }
        }
        composable(
            GestVetRoutes.ClientDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            ClientDetailScreen(onNavigateUp = { navigationController.popBackStack() },
                navigateToDetails = {
                    navigationController.navigate(
                        GestVetRoutes.PetDetails.createRoute(
                            it
                        )
                    )
                }) {
                navigationController.navigate(GestVetRoutes.AddPet.createRoute(it))
            }
        }
        composable(
            GestVetRoutes.AddPet.route,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) {
            AddPetScreen {
                navigationController.popBackStack()
            }
        }
        composable(
            GestVetRoutes.PetDetails.route,
            arguments = listOf(navArgument("petId") { type = NavType.LongType })
        ) {
            PetDetailScreen {
                navigationController.popBackStack()
            }
        }
        composable(GestVetRoutes.Settings.route) {
            SettingScreen {
                navigationController.navigate(GestVetRoutes.Home.route) {
                    popUpTo(GestVetRoutes.Appointment.route) { inclusive = true }
                }
            }
        }
        composable(GestVetRoutes.Home.route) { HomeScreen() }
    }
}