package com.albrodiaz.gestvet.ui.features.home.viewmodels.pets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ConsultationViewModel @Inject constructor(): ViewModel() {
    private val _consultationDate = MutableStateFlow("")
    val consultationDate: StateFlow<String> get() = _consultationDate
    fun setConsultDate(date: String) {
        _consultationDate.value = date
    }

    private val _consultationDetail = MutableStateFlow("")
    val consultationDetail: StateFlow<String> get() = _consultationDetail
    fun setConsultDetail(detail: String) {
        _consultationDetail.value = detail
    }
}