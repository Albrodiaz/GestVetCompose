package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel
import com.albrodiaz.gestvet.ui.theme.md_theme_light_primary

//TODO: Mejorar composable

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddAppointmentDialog(appointmentViewModel: AppointmentViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var ownerText: String by remember { mutableStateOf("") }
    var petText: String by remember { mutableStateOf("") }
    var dateText: String by remember { mutableStateOf("") }
    var hourText: String by remember { mutableStateOf("") }
    var subjectText: String by remember { mutableStateOf("") }
    var detailText: String by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(Modifier.fillMaxSize()) {
            ConstraintLayout(Modifier.fillMaxSize()) {
                val (title, owner, pet, date, hour, subject, detail, saveButton, close) = createRefs()
                IconButton(
                    onClick = { appointmentViewModel.enableDialog(enabled = false) },
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
                    textChange = { ownerText = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                FormTextField(
                    text = petText,
                    label = stringResource(id = R.string.pet),
                    modifier = Modifier.constrainAs(pet) {
                        top.linkTo(owner.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    textChange = { petText = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
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
                    textChange = { dateText = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
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
                    textChange = { hourText = it },
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
                    textChange = { subjectText = it },
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
                    textChange = { detailText = it },
                    maxLines = 15,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )
                Button(
                    onClick = {
                        /*TODO: guardar cita*/
                        appointmentViewModel.enableDialog(enabled = false)
                    },
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    text: String,
    label: String = "",
    modifier: Modifier,
    textChange: (String) -> Unit,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(.8f)
            .padding(vertical = 6.dp),
        value = text,
        label = { Text(text = label) },
        onValueChange = { textChange(it) },
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = md_theme_light_primary,
            cursorColor = md_theme_light_primary,
            focusedLabelColor = md_theme_light_primary,
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun AddScreenTittle(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.addTitle),
        modifier = modifier.padding(vertical = 16.dp, horizontal = 12.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    )
}