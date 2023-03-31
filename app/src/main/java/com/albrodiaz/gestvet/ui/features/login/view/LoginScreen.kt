package com.albrodiaz.gestvet.ui.features.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.LoginButton
import com.albrodiaz.gestvet.ui.features.components.UserPassword
import com.albrodiaz.gestvet.ui.features.components.UserTextField
import com.albrodiaz.gestvet.ui.features.login.viewmodel.LoginViewModel
import com.albrodiaz.gestvet.ui.theme.md_theme_light_primary

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginInputScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    showError: (String) -> Unit,
    navigateRegister: () -> Unit,
    navigateHome: () -> Unit
) {
    val userInput by loginViewModel.userInput.collectAsState()
    val password by loginViewModel.userPassword.collectAsState()
    val enabled by loginViewModel.enabled.collectAsState(false)
    val errorText = stringResource(id = R.string.errorLogin)
    val keyboardController = LocalSoftwareKeyboardController.current
    val isUserLogged by loginViewModel.isUserLogged.collectAsState()

    if (isUserLogged) {
        navigateHome()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoginHeader()
            UserTextField(
                value = userInput,
                placeholder = stringResource(id = R.string.email),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Email
                ),
                valueChange = { loginViewModel.setUserInput(it) })
            UserPassword(value = password, valueChange = { loginViewModel.setUserPassword(it) })
            LoginButton(enabled = enabled, text = stringResource(id = R.string.login)) {
                loginViewModel.login(
                    showError = {
                        showError(errorText)
                        keyboardController?.hide()
                    },
                    openHome = { navigateHome() }
                )
            }
            CreateAccountRow { navigateRegister() }
        }
    }
}

@Preview
@Composable
fun LoginHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppIcon()
        AppTitle()
    }
}

@Composable
fun AppIcon() {
    Image(
        painter = painterResource(id = R.drawable.ic_gestvet),
        contentDescription = "App Icon",
        modifier = Modifier
            .size(100.dp)
            .padding(24.dp)
    )
}

@Composable
fun AppTitle() {
    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Cursive
    )
}

@Composable
fun CreateAccountRow(navigateRegister: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¿Aún no tienes una cuenta?")
        Text(
            text = "Regístrate",
            fontWeight = FontWeight.Bold,
            color = md_theme_light_primary,
            modifier = Modifier.clickable { navigateRegister() })
    }
}