package com.albrodiaz.gestvet.ui.features.home.viewmodels.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albrodiaz.gestvet.data.network.AuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationService: AuthenticationService
) : ViewModel() {

    fun logOut() {
        authenticationService.logOut()
    }

    fun deleteAccount(successDelete: () -> Unit, failureDelete: () -> Unit) =
        viewModelScope.launch {
            if (authenticationService.deleteAccount()) {
                successDelete()
            } else {
                failureDelete()
            }
        }

}