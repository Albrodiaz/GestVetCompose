package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddAppointmentDialog(show: Boolean, appointmentViewModel: AppointmentViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isButtonEnabled by appointmentViewModel.isButtonEnabled.observeAsState(false)
    val ownerText by appointmentViewModel.ownerText.observeAsState("")
    val petText by appointmentViewModel.petText.observeAsState("")
    val dateText by appointmentViewModel.dateText.observeAsState("")
    val hourText by appointmentViewModel.hourText.observeAsState("")
    val subjectText by appointmentViewModel.subjectText.observeAsState("")
    val detailText by appointmentViewModel.detailsText.observeAsState("")

    AddDialog(
        show = show,
        onDismiss = { appointmentViewModel.showDialog(false) }
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (title, owner, pet, date, hour, subject, detail, saveButton, close) = createRefs()
            androidx.compose.material.IconButton(
                onClick = { appointmentViewModel.showDialog(false) },
                modifier = Modifier.constrainAs(close) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }
            AddScreenTittle(modifier = Modifier
                .padding(start = 24.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })
            FormTextField(
                text = ownerText,
                label = stringResource(id = R.string.owner),
                modifier = Modifier.constrainAs(owner) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                textChange = { appointmentViewModel.setOwner(it) }
            )
            FormTextField(
                text = petText,
                label = stringResource(id = R.string.pet),
                modifier = Modifier.constrainAs(pet) {
                    top.linkTo(owner.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                textChange = { appointmentViewModel.setPet(it) }

            )
            FormTextField(
                text = dateText,
                label = stringResource(id = R.string.date),
                modifier = Modifier
                    .fillMaxWidth(.35f)
                    .constrainAs(date) {
                        top.linkTo(pet.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(hour.start)
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                textChange = { appointmentViewModel.setDate(it) },
            )
            FormTextField(
                text = hourText,
                label = stringResource(id = R.string.hour),
                modifier = Modifier
                    .fillMaxWidth(.35f)
                    .constrainAs(hour) {
                        top.linkTo(pet.bottom)
                        start.linkTo(date.end)
                        end.linkTo(parent.end)
                    },
                textChange = { appointmentViewModel.setHour(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            FormTextField(
                text = subjectText,
                label = stringResource(id = R.string.subject),
                modifier = Modifier.constrainAs(subject) {
                    top.linkTo(date.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                textChange = { appointmentViewModel.setSubject(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            FormTextField(
                text = detailText,
                label = stringResource(id = R.string.details),
                modifier = Modifier.constrainAs(detail) {
                    top.linkTo(subject.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                textChange = { appointmentViewModel.setDetails(it) },
                maxLines = 15,
                singleLine = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Button(
                onClick = {
                    appointmentViewModel.apply {
                        addAppointment()
                        showDialog(false)
                    }
                },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .constrainAs(saveButton) {
                        top.linkTo(detail.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}