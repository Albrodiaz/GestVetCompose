package com.albrodiaz.gestvet.ui.features.home.views.appointments

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.core.states.rememberCustomDatePickerState
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AddAppointmentViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentScreen(
    addAppointmentViewModel: AddAppointmentViewModel,
    navigationController: NavHostController,
    appointmentId: Long?
) {
    val isButtonEnabled by addAppointmentViewModel.isButtonEnabled.observeAsState(false)
    val isAddedSuccess by addAppointmentViewModel.isAddedSuccess.observeAsState()
    val showDatePicker by addAppointmentViewModel.showDatePicker.observeAsState(false)
    val showTimePicker by addAppointmentViewModel.showTimePicker.observeAsState(false)
    val isErrorEnabled by addAppointmentViewModel.isErrorEnabled.observeAsState(false)
    val ownerText by addAppointmentViewModel.ownerText.observeAsState("")
    val petText by addAppointmentViewModel.petText.observeAsState("")
    val dateText by addAppointmentViewModel.dateText.observeAsState("")
    val hourText by addAppointmentViewModel.hourText.observeAsState("")
    val subjectText by addAppointmentViewModel.subjectText.observeAsState("")
    val detailText by addAppointmentViewModel.detailsText.observeAsState("")
    val datePickerState = rememberCustomDatePickerState()
    val context = LocalContext.current

    addAppointmentViewModel.setApptId(appointmentId?:0L)

    DateTimeDialog(
        state = datePickerState,
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
        state = null,
        show = showTimePicker,
        onDismiss = { addAppointmentViewModel.setShowTimePicker(false) },
        onConfirm = { addAppointmentViewModel.apply {addAppointmentViewModel.setShowTimePicker(false) } }
    ) {
        AddTimePicker(value = { addAppointmentViewModel.setHour(it) })
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (title, owner, pet, date, hour, subject, detail, saveButton, close) = createRefs()
        val keyboardController = LocalSoftwareKeyboardController.current

        IconButton(
            onClick = { navigationController.navigateUp() },
            modifier = Modifier
                .padding(12.dp)
                .constrainAs(close) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
        ) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
        }
        Text(
            text = if (appointmentId == 0L) stringResource(id = R.string.addTitle) else stringResource(
                id = R.string.modifyAppt
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })
        FormTextField(
            text = ownerText,
            placeholder = stringResource(id = R.string.owner),
            modifier = Modifier.constrainAs(owner) {
                top.linkTo(title.bottom)
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
            isError = isErrorEnabled,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            textChange = { addAppointmentViewModel.setDate(it) },
            trailingIcon = {
                IconButton(onClick = { addAppointmentViewModel.setShowDatePicker(true) }) {
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
            isError = isErrorEnabled,
            trailingIcon = {
                IconButton(onClick = { addAppointmentViewModel.setShowTimePicker(true) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock_24),
                        contentDescription = "show timePicker",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
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
                addAppointmentViewModel.addAppointment()
                isAddedSuccess?.let {
                    if (it) navigationController.navigateUp() else Toast.makeText(
                        context,
                        "Revise los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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