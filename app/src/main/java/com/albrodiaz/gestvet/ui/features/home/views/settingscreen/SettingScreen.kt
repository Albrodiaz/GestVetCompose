package com.albrodiaz.gestvet.ui.features.home.views.settingscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.settings.SettingsViewModel

@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navToLogin: ()-> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Cerrar Sesión", modifier = Modifier.clickable {
            settingsViewModel.logOut()
            navToLogin()
        })
    }
}