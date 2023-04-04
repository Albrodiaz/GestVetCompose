package com.albrodiaz.gestvet.ui.features.home.views.settingscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.ConfirmDeleteDialog
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
    val currentUser by settingsViewModel.currentUser.collectAsState()
    val showDialog by settingsViewModel.showDialog.collectAsState()
    val successDeleteText = stringResource(id = R.string.deleteSuccess)
    val failureDeleteText = stringResource(id = R.string.deleteFailure)
    val emailSent = stringResource(id = R.string.successSent)
    val emailNotSent = stringResource(id = R.string.failureSent)

    DeleteAccountDialog(show = showDialog, dismiss = { settingsViewModel.setShowDialog(false) }) {
        settingsViewModel.deleteAccount(
            successDelete = {
                showMessage(successDeleteText)
                navigate()
            },
            failureDelete = { showMessage(failureDeleteText) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallTextField(value = currentUser.name ?: "", valueChange = {}, enabled = false)
        SmallTextField(value = currentUser.email ?: "", valueChange = {}, enabled = false)
        RecoverPassword {
            settingsViewModel.sendRecoveryEmail(
                successSent = { showMessage(emailSent) },
                failureSent = { showMessage(emailNotSent) }
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(.9f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            LogOutButton {
                settingsViewModel.logOut()
                navigate()
            }
            DeleteAccountButton { settingsViewModel.setShowDialog(true) }
        }
    }
}

@Composable
fun DeleteAccountDialog(show: Boolean, dismiss: () -> Unit, confirm: () -> Unit) {
    ConfirmDeleteDialog(
        show = show,
        title = "¡ATENCIÓN!",
        text = "Se procederá a borrar el usuario y perderá el acceso a su cuenta y los datos asociados",
        onDismiss = { dismiss() },
        onConfirm = { confirm() }
    )
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
fun RecoverPassword(recoveryEmail: () -> Unit) {
    TextButton(onClick = { recoveryEmail() }) {
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