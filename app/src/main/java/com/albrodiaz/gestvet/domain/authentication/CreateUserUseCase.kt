package com.albrodiaz.gestvet.domain.authentication

import com.albrodiaz.gestvet.data.network.AuthenticationService
import com.albrodiaz.gestvet.data.network.UserService
import com.albrodiaz.gestvet.ui.features.login.model.User
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService
) {

    suspend operator fun invoke(user: User): Boolean {
        val accountCreated =
            authenticationService.createAccount(user.email ?: "", user.password ?: "") != null

        return if (accountCreated) {
            userService.createUser(user)
        } else {
            false
        }
    }

}