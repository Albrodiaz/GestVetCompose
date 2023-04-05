package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.showLeftZero
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.core.states.rememberCustomDatePickerState
import com.albrodiaz.gestvet.ui.features.components.*
import com.albrodiaz.gestvet.ui.features.home.viewmodels.appointments.AddAppointmentViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentScreen(
    addAppointmentViewModel: AddAppointmentViewModel,
    showMessage: (String) -> Unit,
    appointmentId: Long?,
    onNavigate: () -> Unit
) {

    val isButtonEnabled by addAppointmentViewModel.isButtonEnabled.collectAsState(false)
    val showDatePicker by addAppointmentViewModel.showDatePicker.collectAsState()
    val showTimePicker by addAppointmentViewModel.showTimePicker.collectAsState()
    val ownerText by addAppointmentViewModel.ownerText.collectAsState()
    val petText by addAppointmentViewModel.petText.collectAsState()
    val dateText by addAppointmentViewModel.dateText.collectAsState()
    val hourText by addAppointmentViewModel.hourText.collectAsState()
    val subjectText by addAppointmentViewModel.subjectText.collectAsState()
    val detailText by addAppointmentViewModel.detailsText.collectAsState()
    val datePickerState = rememberCustomDatePickerState()
    val timePickerState = rememberTimePickerState()

    DateTimeDialog(
        show = showDatePicker,
        onDismiss = { addAppointmentViewModel.setShowDatePicker(false) },
        onConfirm = {
            addAppointmentViewModel.apply {
                setDate(datePickerState.selectedDateMillis!!.toDate())
                setShowDatePicker(false)
            }
        }) {
        AddDatePicker(datePickerState = datePickerState)
    }

    DateTimeDialog(
        show = showTimePicker,
        onDismiss = { addAppointmentViewModel.setShowTimePicker(false) },
        onConfirm = {
            addAppointmentViewModel.apply {
                addAppointmentViewModel.setShowTimePicker(false)
                setHour("${timePickerState.hour.showLeftZero()}:${timePickerState.minute.showLeftZero()}")
            }
        }
    ) {
        AddTimePicker(state = timePickerState)
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (topBar, owner, pet, date, hour, subject, detail, saveButton) = createRefs()
        val keyboardController = LocalSoftwareKeyboardController.current
        val title =
            if (appointmentId == 0L) stringResource(id = R.string.addTitle) else stringResource(
                id = R.string.modifyAppt
            )

        AddTopBar(title = title, modifier = Modifier.constrainAs(topBar) {
            top.linkTo(parent.top)
            bottom.linkTo(owner.top)
        }) {
            onNavigate()
        }
        FormTextField(
            text = ownerText,
            placeholder = stringResource(id = R.string.owner),
            modifier = Modifier.constrainAs(owner) {
                top.linkTo(topBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            textChange = { addAppointmentViewModel.setOwner(it) }
        )
        FormTextField(
            text = petText,
            placeholder = stringResource(id = R.string.pet),
            modifier = Modifier.constrainAs(pet) {
                top.linkTo(owner.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            textChange = { addAppointmentViewModel.setPet(it) }
        )
        FormTextField(
            modifier = Modifier
                .fillMaxWidth(.45f)
                .constrainAs(date) {
                    top.linkTo(pet.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(hour.start)
                },
            text = dateText,
            placeholder = stringResource(id = R.string.date),
            readOnly = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            textChange = { addAppointmentViewModel.setDate(it) },
            trailingIcon = {
                IconButton(onClick = {
                    addAppointmentViewModel.setShowDatePicker(true)
                }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "show datePicker",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )
        FormTextField(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .constrainAs(hour) {
                    top.linkTo(pet.bottom)
                    start.linkTo(date.end)
                    end.linkTo(parent.end)
                },
            text = hourText,
            placeholder = stringResource(id = R.string.hour),
            textChange = { addAppointmentViewModel.setHour(it) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    addAppointmentViewModel.setShowTimePicker(true)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock_24),
                        contentDescription = "show timePicker",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        FormTextField(
            text = subjectText,
            placeholder = stringResource(id = R.string.subject),
            modifier = Modifier.constrainAs(subject) {
                top.linkTo(date.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textChange = { addAppointmentViewModel.setSubject(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            )
        )
        FormTextField(
            text = detailText,
            placeholder = stringResource(id = R.string.details),
            modifier = Modifier.constrainAs(detail) {
                top.linkTo(subject.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textChange = { addAppointmentViewModel.setDetails(it) },
            maxLines = 15,
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Button(
            onClick = {
                addAppointmentViewModel.saveAppointment(
                    success = {
                        showMessage("Cita guardada")
                        onNavigate()
                    },
                    dateUnavailable = { showMessage("Fecha y hora no disponible") },
                    onError = { showMessage("Error al guardar la cita") }
                )
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .constrainAs(saveButton) {
                    top.linkTo(detail.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}