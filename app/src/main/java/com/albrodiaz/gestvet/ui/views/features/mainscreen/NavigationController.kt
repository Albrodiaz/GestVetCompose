package com.albrodiaz.gestvet.ui.views.features.mainscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.albrodiaz.gestvet.ui.views.features.addscreen.AddScreen
import com.albrodiaz.gestvet.ui.views.features.appointments.AppointmentScreen
import com.albrodiaz.gestvet.ui.views.features.clients.ClientScreen
import com.albrodiaz.gestvet.ui.views.models.Routes
import com.albrodiaz.gestvet.ui.views.features.searchscreen.SearchScreen

@Composable
fun NavController(navigationController: NavController, paddingValues: Dp? = null) {
    NavHost(navController = navigationController as NavHostController, startDestination = Routes.Appointment.route) {
        composable(Routes.Appointment.route) { AppointmentScreen(paddingValues?:0.dp) }
        composable(Routes.Add.route) { AddScreen() }
        composable(Routes.Client.route) { ClientScreen() }
        composable(Routes.Search.route) { SearchScreen() }
    }
}