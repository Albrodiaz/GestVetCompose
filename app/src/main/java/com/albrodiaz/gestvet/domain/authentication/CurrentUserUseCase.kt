package com.albrodiaz.gestvet.domain.authentication

import com.albrodiaz.gestvet.data.network.AuthenticationService
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    operator fun invoke() = authenticationService.hasUser

}