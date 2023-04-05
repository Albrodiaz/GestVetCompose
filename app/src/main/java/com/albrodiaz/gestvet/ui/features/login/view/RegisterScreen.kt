package com.albrodiaz.gestvet.ui.features.login.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.isValidEmail
import com.albrodiaz.gestvet.ui.features.components.LoadingScreen
import com.albrodiaz.gestvet.ui.features.components.LoginButton
import com.albrodiaz.gestvet.ui.features.components.UserPassword
import com.albrodiaz.gestvet.ui.features.components.UserTextField
import com.albrodiaz.gestvet.ui.features.login.viewmodel.RegisterViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    showResult: (String) -> Unit,
    navigateBack: () -> Unit
) {
    registerViewModel.apply {
        val userName by userName.collectAsState()
        val email by email.collectAsState()
        val password by password.collectAsState()
        val repeatedPass by repeatedPass.collectAsState()
        val btnEnabled by enabled.collectAsState(false)
        val passError by passwordError.collectAsState(false)
        val emailError = email.length > 9 && !email.isValidEmail()
        val showVerify by showVerify.collectAsState()
        val keyboardManager = LocalSoftwareKeyboardController.current
        val successText = stringResource(id = R.string.createSuccess)
        val failureText = stringResource(id = R.string.createFailure)
        val verifiedAccountText = stringResource(id = R.string.verifiedAccount)

        if (showVerify) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingScreen(text = stringResource(id = R.string.waitingVerified))
            }
        } else {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RegisterTopBar { navigateBack() }
                UserTextField(
                    value = userName,
                    valueChange = { setUserName(it) },
                    placeholder = stringResource(id = R.string.fullName),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                )
                UserTextField(
                    value = email,
                    valueChange = { setEmail(it) },
                    placeholder = stringResource(id = R.string.email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailError,
                    supportingText = if (emailError) stringResource(id = R.string.invalidEmail) else ""
                )
                UserPassword(
                    value = password,
                    valueChange = { setPassword(it) },
                    isError = passError,
                    supportingText = if (passError) stringResource(id = R.string.equalPass) else stringResource(
                        id = R.string.passLength
                    )
                )
                UserPassword(
                    value = repeatedPass,
                    valueChange = { setRepeatedPass(it) },
                    placeholder = stringResource(id = R.string.repeatPassword),
                    isError = passError,
                    supportingText = if (passError)
                        stringResource(id = R.string.equalPass) else stringResource(id = R.string.passLength)
                )
                LoginButton(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(id = R.string.createAccount),
                    enabled = btnEnabled
                ) {
                    keyboardManager?.hide()
                    createUser(
                        onSuccess = { showResult(successText) },
                        failure = { showResult(failureText) },
                        completed = { 
                            showResult(verifiedAccountText)
                            navigateBack() 
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTopBar(navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(text = stringResource(id = R.string.register)) },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.toLogin)
                )
            }
        }
    )
}