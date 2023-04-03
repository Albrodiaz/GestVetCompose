package com.albrodiaz.gestvet.domain.user

import com.albrodiaz.gestvet.data.network.UserService
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userService: UserService
) {

    suspend operator fun invoke() = userService.getUserData()

}