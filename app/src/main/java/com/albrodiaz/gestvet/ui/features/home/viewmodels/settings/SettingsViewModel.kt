package com.albrodiaz.gestvet.ui.features.home.viewmodels.settings

import androidx.lifecycle.ViewModel
import com.albrodiaz.gestvet.data.network.AuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationService: AuthenticationService
): ViewModel() {

    fun logOut() {
        authenticationService.logOut()
    }

}