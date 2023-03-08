package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.theme.md_theme_light_error
import com.albrodiaz.gestvet.ui.theme.md_theme_light_primary
import com.albrodiaz.gestvet.ui.theme.md_theme_light_surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    text: String,
    label: String = "",
    modifier: Modifier,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textChange: (String) -> Unit
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteButton(
    modifier: Modifier,
    onDeleteAppointment: () -> Unit,
    swipeableState: SwipeableState<Int>? = null
) {
    Box(modifier = modifier.fillMaxHeight()) {
        AnimatedVisibility(
            visible = swipeableState?.targetValue == 1 && swipeableState.progress.fraction > 0.5,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
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

@Composable
fun AppointmentCard(modifier: Modifier, content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(135.dp)
            .padding(6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        content()
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
            exit = slideOutVertically { with(density) { 60.dp.roundToPx() } }
        ) {
            FloatingActionButton(modifier = modifier.size(45.dp), onClick = { showDialog() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false
            )
        ) {
            Surface(
                Modifier
                    .fillMaxSize()
                    .background(md_theme_light_surface)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(show: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
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