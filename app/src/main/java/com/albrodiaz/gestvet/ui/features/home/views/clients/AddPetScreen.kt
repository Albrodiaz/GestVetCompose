package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.core.states.rememberCustomDatePickerState
import com.albrodiaz.gestvet.ui.features.components.DateTimeDialog
import com.albrodiaz.gestvet.ui.features.components.FormTextField
import com.albrodiaz.gestvet.ui.features.home.viewmodels.pets.AddPetViewModel
import com.albrodiaz.gestvet.ui.theme.md_theme_light_primaryContainer

@Composable
fun AddPetScreen(addPetViewModel: AddPetViewModel = hiltViewModel(), onClose: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        AddPetHeader(onClose = { onClose() })
        AddPetForm(
            addPetViewModel = addPetViewModel,
            onSave = {
                addPetViewModel.addPet()
                onClose()
            }
        )
    }
}

@Composable
private fun AddPetHeader(onClose: () -> Unit) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        val (title, close) = createRefs()
        Text(
            text = stringResource(id = R.string.addPet),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 12.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })
        IconButton(
            onClick = { onClose() },
            modifier = Modifier.constrainAs(close) {
                end.linkTo(parent.end)
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
            }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = ""
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetForm(
    addPetViewModel: AddPetViewModel,
    onSave: () -> Unit
) {
    addPetViewModel.apply {

        val datePickerState = rememberCustomDatePickerState()
        val name by name.collectAsState()
        val birthDate by birthDate.collectAsState()
        val breed by breed.collectAsState()
        val chipNumber by chipNumber.collectAsState()
        val passport by passport.collectAsState()
        val color by color.collectAsState()
        val neutered by neutered.collectAsState()
        val showDatePicker by showDatePicker.collectAsState()

        DateTimeDialog(
            show = showDatePicker,
            onDismiss = { setShowDatePicker(false) },
            onConfirm = {
                setBirthDate(datePickerState.selectedDateMillis?.toDate() ?: "")
                setShowDatePicker(false)
            }
        ) {
            DatePicker(state = datePickerState)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FormTextField(
                text = name,
                textChange = { setName(it) },
                placeholder = stringResource(id = R.string.name),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            FormTextField(
                text = birthDate,
                textChange = { setBirthDate(it) },
                placeholder = stringResource(id = R.string.birthDate),
                readOnly = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                trailingIcon = {
                    IconButton(onClick = { setShowDatePicker(true) }) {
                        Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                    }
                }
            )
            FormTextField(
                text = breed,
                textChange = { setBreed(it) },
                placeholder = stringResource(id = R.string.breed),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            FormTextField(
                text = chipNumber,
                textChange = { setChipNumber(it) },
                placeholder = stringResource(id = R.string.chipNumber),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            FormTextField(
                text = passport,
                textChange = { setPassport(it) },
                placeholder = stringResource(id = R.string.passportNumber),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            FormTextField(
                text = color,
                textChange = { setColor(it) },
                placeholder = stringResource(id = R.string.color)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.neutered),
                    fontSize = 16.sp
                )
                NeuteredSwitch(neutered = neutered, onCheckedChange = { setNeutered(it) })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { onSave() }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}

@Composable
private fun NeuteredSwitch(neutered: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val icon: (@Composable () -> Unit) = if (neutered) {
        {
            Icon(imageVector = Icons.Filled.Check,contentDescription = null)
        }
    } else {
        {
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        }
    }
    Switch(
        checked = neutered,
        onCheckedChange = { onCheckedChange(it) },
        thumbContent = icon,
        colors = SwitchDefaults.colors(
            checkedTrackColor = md_theme_light_primaryContainer
        )
    )
}