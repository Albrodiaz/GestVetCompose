package com.albrodiaz.gestvet.ui.features.login.viewmodel

import androidx.lifecycle.ViewModel
import com.albrodiaz.gestvet.core.extensions.isValidEmail
import com.albrodiaz.gestvet.core.extensions.isValidPass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> get() = _userName
    fun setUserName(name: String) {
        _userName.value = name
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email
    fun setEmail(email: String) {
        _email.value = email
    }

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password
    fun setPassword(pass: String) {
        _password.value = pass
    }

    private val _repeatedPass = MutableStateFlow("")
    val repeatedPass: StateFlow<String> get() = _repeatedPass
    fun setRepeatedPass(repeated: String) {
        _repeatedPass.value = repeated
    }

    private val validUserInfo = userName.combine(email) { name, email ->
        return@combine name.isNotEmpty() && email.isValidEmail()
    }

    private val validPassword = password.combine(repeatedPass) { pass, repeated ->
        return@combine pass.isValidPass() && repeated == pass
    }

    val passwordError = password.combine(repeatedPass) { pass, repeat ->
        return@combine pass.length > 8 && repeat.length > 8 && pass != repeat
    }

    val enabled = validUserInfo.combine(validPassword) {user, pass ->
        return@combine user && pass
    }
}