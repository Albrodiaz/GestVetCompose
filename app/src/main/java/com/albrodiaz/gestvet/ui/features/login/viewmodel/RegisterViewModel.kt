package com.albrodiaz.gestvet.ui.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.core.extensions.isValidEmail
import com.albrodiaz.gestvet.core.extensions.isValidPass
import com.albrodiaz.gestvet.domain.authentication.CreateUserUseCase
import com.albrodiaz.gestvet.domain.authentication.SendVerificationUseCase
import com.albrodiaz.gestvet.domain.authentication.VerifyEmailUseCase
import com.albrodiaz.gestvet.ui.features.login.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val sendVerificationUseCase: SendVerificationUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {

    private val _showVerify = MutableStateFlow(false)
    val showVerify: StateFlow<Boolean> get() = _showVerify
    fun setShowVerify(value: Boolean) {
        _showVerify.value = value
    }

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

    val enabled = validUserInfo.combine(validPassword) { user, pass ->
        return@combine user && pass
    }

    private fun sendVerification() {
        viewModelScope.launch {
            sendVerificationUseCase.invoke()
        }
    }

    fun createUser(onSuccess: () -> Unit, failure: () -> Unit) {
        _showVerify.value = true

        val createdUser = User(
            name = userName.value,
            password = password.value,
            email = email.value
        )
        viewModelScope.launch {
            try {
                val successRegister = createUserUseCase.invoke(createdUser)
                sendVerification()
                verifyEmailUseCase.invoke().collect { verified ->
                    if (successRegister && verified) {
                        onSuccess()
                    }
                }
            } catch (error: Throwable) {
                failure()
            }
        }
    }
}