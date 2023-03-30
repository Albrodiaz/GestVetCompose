package com.albrodiaz.gestvet.ui.features.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.login.viewmodel.LoginViewModel
import com.albrodiaz.gestvet.ui.theme.Shapes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginInputScreen(loginViewModel: LoginViewModel = hiltViewModel(),showError: (String)-> Unit ,navigate: () -> Unit) {
    val userInput by loginViewModel.userInput.collectAsState()
    val password by loginViewModel.userPassword.collectAsState()
    val enabled by loginViewModel.enabled.collectAsState(false)
    val errorText = stringResource(id = R.string.errorLogin)
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LoginHeader()
        UserTextField(value = userInput, valueChange = { loginViewModel.setUserInput(it) })
        UserPassword(value = password, valueChange = { loginViewModel.setUserPassword(it) })
        LoginButton(enabled = enabled) {
            loginViewModel.login(
                showError = {
                    showError(errorText)
                    keyboardController?.hide()
                },
                openHome = { navigate() }
            )
        }
    }
}

@Composable
fun LoginHeader() {
    AppIcon()
    AppTitle()
    Spacer(modifier = Modifier.size(48.dp))
}

@Composable
fun AppIcon() {
    Image(
        painter = painterResource(id = R.drawable.ic_gestvet),
        contentDescription = "App Icon",
        modifier = Modifier.size(100.dp)
    )
}

@Composable
fun AppTitle() {
    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Default
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTextField(value: String, valueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = valueChange,
        placeholder = { Text(text = stringResource(id = R.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        shape = Shapes.medium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPassword(value: String, valueChange: (String) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val icon = if (!visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    TextField(
        value = value,
        onValueChange = valueChange,
        placeholder = { Text(text = stringResource(id = R.string.password)) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        shape = Shapes.medium,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(imageVector = icon, contentDescription = "Toggle password")
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun LoginButton(enabled: Boolean, doLogin: () -> Unit) {
    Button(
        onClick = doLogin,
        shape = Shapes.medium,
        enabled = enabled
    ) {
        Text(
            text = stringResource(id = R.string.login), style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                fontSize = 16.sp
            )
        )
    }
}