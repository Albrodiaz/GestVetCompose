package com.albrodiaz.gestvet.domain.authentication

import com.albrodiaz.gestvet.data.network.AuthenticationService
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke(email: String, password: String, onResult: (Throwable?)->Unit) =
        authenticationService.login(email,password) {
            onResult(it)
        }

}