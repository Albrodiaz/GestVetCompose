package com.albrodiaz.gestvet.ui.features.home.views.appointments

import android.widget.TimePicker
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.getColor
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.hourFormatter
import com.albrodiaz.gestvet.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    text: String,
    placeholder: String = "",
    modifier: Modifier,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 6.dp),
        value = text,
        placeholder = { Text(text = placeholder) },
        onValueChange = { textChange(it) },
        maxLines = maxLines,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            cursorColor = md_theme_light_primary,
            focusedLabelColor = md_theme_light_primary,
            containerColor = md_theme_light_inverseOnSurface
        )
    )
}

@Composable
fun DateTextField(text: String, modifier: Modifier) {
    Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = modifier)
}

@Composable
fun AppointmentTextField(text: String, modifier: Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier.padding(start = 6.dp)
    )
}

@Composable
fun ConfirmDeleteDialog(show: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "¿Eliminar la cita?") },
            text = { Text(text = "Una vez eliminada no se podrá recuperar.") },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Cancelar")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

@Composable
fun AnimatedAddFab(modifier: Modifier, visible: Boolean, showDialog: () -> Unit) {
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { with(density) { 60.dp.roundToPx() } },
            exit = slideOutVertically { with(density) { 100.dp.roundToPx() } }
        ) {
            FloatingActionButton(modifier = modifier.size(45.dp), onClick = { showDialog() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteButton(
    modifier: Modifier,
    swipeableState: SwipeableState<Int>? = null,
    onDeleteAppointment: () -> Unit
) {
    val density = LocalDensity.current
    Box(modifier = modifier.fillMaxHeight()) {
        AnimatedVisibility(
            visible = (swipeableState?.targetValue == 1) && (swipeableState.progress.fraction > 0.5),
            enter = fadeIn(animationSpec = tween(1500)) + slideInHorizontally { with(density) { 100.dp.roundToPx() } },
            exit = fadeOut(animationSpec = tween(1500)) + slideOutHorizontally { with(density) { 50.dp.roundToPx() } }
        ) {
            FloatingActionButton(
                onClick = { onDeleteAppointment() },
                containerColor = md_theme_light_error
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "", tint = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DateTimeDialog(
    state: T? = null,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit,
    content: @Composable (T?) -> Unit
) {
    if (show) {
        DatePickerDialog(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm(null) }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        ) {
            if (state != null) content(state) else content(null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDatePicker(datePickerState: DatePickerState) {
    DatePicker(
        state = datePickerState,
        dateFormatter = DatePickerFormatter(selectedDateSkeleton = "ddMMyyyy"),
        showModeToggle = true
    )
}



@Composable
fun AddTimePicker(value: (String) -> Unit) {
    AndroidView(factory = {
        TimePicker(it).apply {
            setBackgroundColor(getColor(it, R.color.surface_light))
            setIs24HourView(true)
            this.alpha = .81f
            setOnTimeChangedListener { _, _, _ ->
                value("${hour.hourFormatter()}:${minute.hourFormatter()}")
            }
        }
    })
}
/*  TODO: A la espera de versión estable de Timepicker Compose

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompTimePicker() {
    val state = rememberTimePickerState()
    GestVetTheme {
        TimePicker(
            state = state,
            modifier = Modifier,
            colors = TimePickerDefaults.colors(
                //Propiedades del TimePicker
            ),
        )
    }
}*/