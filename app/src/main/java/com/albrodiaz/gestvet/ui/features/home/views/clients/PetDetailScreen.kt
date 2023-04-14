package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.isValidDate
import com.albrodiaz.gestvet.core.extensions.showLeftZero
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.core.states.rememberCustomDatePickerState
import com.albrodiaz.gestvet.ui.features.components.*
import com.albrodiaz.gestvet.ui.features.home.models.ConsultationModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.pets.DetailPetViewModel
import com.albrodiaz.gestvet.ui.theme.Shapes

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    detailPetViewModel: DetailPetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isEditActive by detailPetViewModel.editEnabled.collectAsState()
    val showConfirmDialog by detailPetViewModel.showDialog.collectAsState()
    val consultations by detailPetViewModel.consultations.collectAsState()
    val datePickerState = rememberCustomDatePickerState()
    val timePickerState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    ConfirmDeleteDialog(
        title = stringResource(id = R.string.confirmDelete),
        text = stringResource(id = R.string.deleteDescription),
        show = showConfirmDialog,
        onDismiss = { detailPetViewModel.setShowDialog(false) },
        onConfirm = {
            detailPetViewModel.deletePet()
            onNavigateBack()
        }
    )

    DateTimeDialog(
        show = showDatePicker,
        onDismiss = { showDatePicker = false },
        onConfirm = {
            detailPetViewModel.setConsultDate(datePickerState.selectedDateMillis!!.toDate())
            showDatePicker = false
        }
    ) {
        AddDatePicker(datePickerState = datePickerState)
    }

    DateTimeDialog(
        show = showTimePicker,
        onDismiss = { showTimePicker = false },
        onConfirm = {
            detailPetViewModel.setConsultationHour("${timePickerState.hour}:${timePickerState.minute.showLeftZero()}")
            showTimePicker = false
        }
    ) {
        AddTimePicker(state = timePickerState)
    }

    ConsultationDialog(
        detailPetViewModel = detailPetViewModel,
        showDatePicker = { showDatePicker = true },
        showTimePicker = { showTimePicker = true }
    ) {
        detailPetViewModel.setConsultationDialog(false)
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        DetailsTopBar(
            title = stringResource(id = R.string.pet),
            editEnabled = isEditActive,
            onDelete = { detailPetViewModel.setShowDialog(true) },
            onEdit = {
                detailPetViewModel.setEdit(!isEditActive)
                if (isEditActive) {
                    detailPetViewModel.updateData()
                    savedToast(context)
                }
            },
            onNavigateBack = { onNavigateBack() }
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DetailCard {
                PetDetailSection(detailPetViewModel, enabled = isEditActive)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.consultations),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { detailPetViewModel.setConsultationDialog(true) }) {
                    Text(text = stringResource(id = R.string.add))
                }
            }

            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(consultations, key = { it.id ?: -1 }) {
                    var expanded by remember { mutableStateOf(false) }
                    ConsultationItem(
                        consultation = it,
                        visible = expanded,
                        modifier = Modifier
                            .clickable { expanded = !expanded }
                            .animateItemPlacement(),
                        index = consultations.reversed().indexOf(it) + 1
                    ) {
                        detailPetViewModel.deleteConsultation(it.id ?: -1)
                    }
                    AnimatedVisibility(visible = expanded) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 12.dp)
                        ) {
                            SmallTextField(
                                value = "${it.description}",
                                valueChange = {},
                                enabled = false,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Divider(modifier = Modifier.padding(horizontal = 2.dp))
                }
            }
        }
    }
}

@Composable
fun ConsultationItem(
    modifier: Modifier,
    visible: Boolean,
    consultation: ConsultationModel,
    index: Int,
    onDeleteItem: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.2f)
    ) {
        ListItem(
            headlineContent = { Text(text = "${consultation.date}, ${consultation.hour}") },
            overlineContent = { Text(text = "${stringResource(id = R.string.consultation)}: $index") },
            trailingContent = {
                AnimatedVisibility(visible = visible) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete),
                        modifier = modifier.clickable { onDeleteItem() })
                }
            }
        )
    }
}

@Composable
private fun PetDetailSection(detailPetViewModel: DetailPetViewModel, enabled: Boolean) {
    detailPetViewModel.apply {
        val name by petName.collectAsState()
        val birthDate by petBirth.collectAsState()
        val breed by petBreed.collectAsState()
        val color by petColor.collectAsState()
        val chipNumber by petChip.collectAsState()
        val passport by petPassport.collectAsState()
        val neutered by petNeutered.collectAsState()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            DetailTextRow(
                descriptionText = stringResource(id = R.string.name),
                enabled = enabled,
                text = name
            ) {
                setPetName(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.birthDate),
                enabled = enabled,
                text = birthDate
            ) {
                setPetBirth(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.breed),
                enabled = enabled,
                text = breed
            ) {
                setPetBreed(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.color),
                enabled = enabled,
                text = color
            ) {
                setPetColor(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.chipNumber),
                enabled = enabled,
                text = chipNumber
            ) {
                setPetChip(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.passportNumber),
                enabled = enabled,
                text = passport
            ) {
                setPetPassport(it)
            }
            NeuteredRow(
                neutered = neutered,
                enabled = enabled,
                onCheckChange = { setPetNeutered(it) })
        }
    }
}


@Composable
private fun DetailTextRow(
    enabled: Boolean,
    descriptionText: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        PetDetailText(text = descriptionText)
        SmallTextField(
            value = text,
            enabled = enabled,
            valueChange = { onValueChange(it) }
        )
    }
}

@Composable
private fun NeuteredRow(neutered: Boolean, enabled: Boolean, onCheckChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        PetDetailText(text = stringResource(id = R.string.neutered))
        NeuteredSwitch(
            neutered = neutered,
            enabled = enabled,
            onCheckedChange = { onCheckChange(it) })
    }
}

@Composable
private fun PetDetailText(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(.4f),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            fontSize = 14.sp,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationDialog(
    detailPetViewModel: DetailPetViewModel,
    showDatePicker: () -> Unit,
    showTimePicker: () -> Unit,
    onDismiss: () -> Unit
) {

    val show by detailPetViewModel.showConsultationDialog.collectAsState()
    val date by detailPetViewModel.consultationDate.collectAsState()
    val hour by detailPetViewModel.consultationHour.collectAsState()
    val details by detailPetViewModel.consultationDetail.collectAsState()

    if (show) {
        AlertDialog(onDismissRequest = onDismiss) {
            Surface(
                shape = Shapes.large,
                shadowElevation = 4.dp,
                modifier = Modifier.wrapContentSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.addConsultation),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                    FormTextField(
                        text = date,
                        textChange = { detailPetViewModel.setConsultDate(it) },
                        readOnly = true,
                        placeholder = stringResource(id = R.string.date),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker() }) {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = stringResource(id = R.string.date)
                                )
                            }
                        }
                    )
                    FormTextField(
                        text = hour,
                        textChange = { detailPetViewModel.setConsultationHour(it) },
                        readOnly = true,
                        placeholder = stringResource(id = R.string.hour),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clock_24),
                                    contentDescription = stringResource(id = R.string.date)
                                )
                            }
                        }
                    )
                    FormTextField(
                        text = details,
                        textChange = { detailPetViewModel.setConsultDetail(it) },
                        singleLine = false,
                        maxLines = 50,
                        placeholder = stringResource(id = R.string.nonOptionalDetail),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.7f)
                    )
                    TextButton(onClick = {
                        detailPetViewModel.addConsultation()
                        detailPetViewModel.setConsultDetail("")
                        onDismiss()
                    }, enabled = date.isValidDate() && details.length > 5 && hour.isNotEmpty()) {
                        Text(text = stringResource(id = R.string.save))
                    }
                    Text(text = stringResource(id = R.string.modifyConsultation), fontSize = 9.sp)
                }
            }
        }
    }
}