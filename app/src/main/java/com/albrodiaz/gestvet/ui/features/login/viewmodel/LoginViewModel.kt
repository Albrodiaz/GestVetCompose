package com.albrodiaz.gestvet.ui.features.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
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
        return@combine Patterns.EMAIL_ADDRESS.matcher(input).matches()
                && password.length > 8
    }
}