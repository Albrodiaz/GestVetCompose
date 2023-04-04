package com.albrodiaz.gestvet.domain.authentication

import com.albrodiaz.gestvet.data.network.AuthenticationService
import javax.inject.Inject

class SendVerificationUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke() = authenticationService.sendVerificationEmail()

}