package com.albrodiaz.gestvet.ui.views.features.mainscreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.albrodiaz.gestvet.ui.theme.views.features.addscreen.AddScreen
import com.albrodiaz.gestvet.ui.views.features.appointments.AppointmentScreen
import com.albrodiaz.gestvet.ui.views.features.clients.ClientScreen
import com.albrodiaz.gestvet.ui.theme.views.models.Routes
import com.albrodiaz.gestvet.ui.theme.views.features.searchscreen.SearchScreen

@Composable
fun NavController(navigationController: NavController) {
    NavHost(navController = navigationController as NavHostController, startDestination = Routes.Appointment.route) {
        composable(Routes.Appointment.route) { AppointmentScreen() }
        composable(Routes.Add.route) { AddScreen() }
        composable(Routes.Client.route) { ClientScreen() }
        composable(Routes.Search.route) { SearchScreen() }
    }
}