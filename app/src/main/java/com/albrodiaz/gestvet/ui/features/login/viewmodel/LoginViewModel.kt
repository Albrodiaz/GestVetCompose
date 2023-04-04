package com.albrodiaz.gestvet.ui.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.core.extensions.isValidEmail
import com.albrodiaz.gestvet.core.extensions.isValidPass
import com.albrodiaz.gestvet.data.network.AuthenticationService
import com.albrodiaz.gestvet.domain.authentication.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authenticationService: AuthenticationService
) : ViewModel() {

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> get() = _isUserLogged

    init {
        _isUserLogged.value = authenticationService.hasUser
    }

    private val _resetEmail = MutableStateFlow("")
    val resetEmail: StateFlow<String> get() = _resetEmail
    fun setResetEmail(email: String) {
        _resetEmail.value = email
    }

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog
    fun setShowDialog(value: Boolean) {
        _showDialog.value = value
    }

    private val _userInput = MutableStateFlow("")
    val userInput: StateFlow<String> get() = _userInput
    fun setUserInput(input: String) {
        _userInput.value = input
    }

    private val _userPassword = MutableStateFlow("")
    val userPassword: StateFlow<String> get() = _userPassword
    fun setUserPassword(password: String) {
        _userPassword.value = password
    }

    val enabled = userInput.combine(userPassword) { input, password ->
        return@combine input.isValidEmail() && password.isValidPass()
    }

    fun login(showError: () -> Unit, openHome: () -> Unit) {
        viewModelScope.launch {
            try {
                loginUseCase.invoke(userInput.value, userPassword.value) {
                    if (it == null) {
                        openHome()
                    }
                }
            } catch (error: Throwable) {
                showError()
            }
        }
    }

    fun recoverPassword(email: String, resetResult: (String)-> Unit) {
        viewModelScope.launch {
            val emailSent = authenticationService.recoverPassword(email)
            if (emailSent) {
                resetResult("Email enviado")
            } else {
                resetResult("Error al enviar el email")
            }
        }
    }
}