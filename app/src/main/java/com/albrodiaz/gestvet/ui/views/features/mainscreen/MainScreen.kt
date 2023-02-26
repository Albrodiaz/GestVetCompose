package com.albrodiaz.gestvet.ui.views.features.mainscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.albrodiaz.gestvet.ui.views.models.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        bottomBar = { MainBottomNav(navController) }
    ) {
        NavController(navController, it.calculateBottomPadding())
    }
}

@Composable
fun MainBottomNav(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar(Modifier.fillMaxWidth()) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "") },
            label = { Text("Citas") },
            selected = selectedItem == 0,
            onClick = {
                navController.navigate(Routes.Appointment.route)
                selectedItem = 0

            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
            label = { Text("Añadir") },
            selected = selectedItem == 1,
            onClick = {
                navController.navigate(Routes.Add.route)
                selectedItem = 1
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "") },
            label = { Text("Clientes") },
            selected = selectedItem == 2,
            onClick = {
                navController.navigate(Routes.Client.route)
                selectedItem = 2
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "") },
            label = { Text("Buscar") },
            selected = selectedItem == 3,
            onClick = {
                navController.navigate(Routes.Search.route)
                selectedItem = 3
            }
        )
    }
}