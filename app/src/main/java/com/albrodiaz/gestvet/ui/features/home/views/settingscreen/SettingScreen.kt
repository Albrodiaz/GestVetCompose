package com.albrodiaz.gestvet.ui.features.home.views.settingscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.SmallTextField
import com.albrodiaz.gestvet.ui.features.home.viewmodels.settings.SettingsViewModel
import com.albrodiaz.gestvet.ui.theme.Shapes
import com.albrodiaz.gestvet.ui.theme.md_theme_light_error
import com.albrodiaz.gestvet.ui.theme.md_theme_light_primary

@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
    navToLogin: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsTopBar()
        SettingsItems(settingsViewModel = settingsViewModel, showMessage = { showSnackBar(it) }) {
            navToLogin()
        }
    }
}

@Composable
private fun SettingsItems(
    settingsViewModel: SettingsViewModel,
    showMessage: (String) -> Unit,
    navigate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallTextField(value = "Nombre completo usuario", valueChange = {}, enabled = false)
        SmallTextField(value = "Email usuario", valueChange = {}, enabled = false)
        RecoverPassword()
        Spacer(modifier = Modifier.fillMaxHeight(.9f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            LogOutButton {
                settingsViewModel.logOut()
                navigate()
            }
            DeleteAccountButton {
                settingsViewModel.deleteAccount(
                    successDelete = {
                        showMessage("Usuario borrado con éxito")
                        navigate()
                    },
                    failureDelete = { showMessage("Error al eliminar el usuario") }
                )
            }
        }
    }
}

@Composable
private fun LogOutButton(onLogOut: () -> Unit) {
    OutlinedButton(
        onClick = { onLogOut() },
        colors = ButtonDefaults.buttonColors(
            contentColor = md_theme_light_primary,
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, md_theme_light_primary),
        shape = Shapes.medium
    ) {
        Text(text = stringResource(id = R.string.logout))
    }
}

@Composable
private fun DeleteAccountButton(onDelete: () -> Unit) {
    OutlinedButton(
        onClick = { onDelete() },
        colors = ButtonDefaults.buttonColors(
            contentColor = md_theme_light_error,
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, md_theme_light_error),
        shape = Shapes.medium
    ) {
        Text(text = stringResource(id = R.string.deleteAccount))
    }
}

@Composable
fun RecoverPassword() {
    TextButton(onClick = { /*Solicitar nueva contraseña*/ }) {
        Text(text = stringResource(id = R.string.newPass))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}


/*TODO: Implementar borrado de cuenta y validación de registro por email*/