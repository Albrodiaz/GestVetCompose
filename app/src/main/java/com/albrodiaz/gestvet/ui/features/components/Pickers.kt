package com.albrodiaz.gestvet.ui.features.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.theme.GestVetTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    if (show) {
        DatePickerDialog(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDatePicker(datePickerState: DatePickerState) {
    DatePicker(
        state = datePickerState,
        dateFormatter = DatePickerFormatter(selectedDateSkeleton = "ddMMyyyy"),
        showModeToggle = true,
        dateValidator = { it >= Date().time - 86400000 }
    )
}

/*  TODO: A la espera de versión estable de Timepicker Compose*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimePicker(state: TimePickerState) {
    GestVetTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            TimePicker(state = state)
        }
    }
}