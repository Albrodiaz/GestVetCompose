package com.albrodiaz.gestvet.ui.features.home.views.settingscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.home.viewmodels.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navToLogin: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "") },
            actions = {
                IconButton(
                    onClick = {
                        settingsViewModel.logOut()
                        navToLogin()
                    }
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = stringResource(id = R.string.logout)
                    )
                }
            }
        )
        Text(text = "Settings Screen")
    }
}