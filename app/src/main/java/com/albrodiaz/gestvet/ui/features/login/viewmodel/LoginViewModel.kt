package com.albrodiaz.gestvet.ui.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.core.extensions.isValidEmail
import com.albrodiaz.gestvet.core.extensions.isValidPass
import com.albrodiaz.gestvet.domain.authentication.CurrentUserUseCase
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
    currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> get() = _isUserLogged

    init {
        _isUserLogged.value = currentUserUseCase.invoke()
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
}