package com.albrodiaz.gestvet.ui.features.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.theme.md_theme_light_primary
import com.albrodiaz.gestvet.ui.theme.md_theme_light_surfaceVariant

@Composable
fun ConfirmDeleteDialog(
    title: String,
    text: String,
    textColor: Color = md_theme_light_primary,
    show: Boolean,
    containerColor: Color = md_theme_light_surfaceVariant,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "OK", color = textColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(id = R.string.cancel), color = textColor)
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            containerColor = containerColor
        )
    }
}