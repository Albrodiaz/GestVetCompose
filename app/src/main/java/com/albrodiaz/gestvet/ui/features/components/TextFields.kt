package com.albrodiaz.gestvet.ui.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.albrodiaz.gestvet.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String = "",
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
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
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            cursorColor = md_theme_light_primary,
            focusedLabelColor = md_theme_light_primary
        )
    )
}

@Composable
fun DetailsTextfield(
    text: String,
    modifier: Modifier = Modifier,
    valueChange: (String) -> Unit,
    enabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        value = text,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        onValueChange = { valueChange(it) },
        singleLine = true,
        enabled = enabled,
        maxLines = 1,
        textStyle = TextStyle(
            color = if (isSystemInDarkTheme()) md_theme_dark_onSurface else md_theme_light_onSurface
        ),
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(md_theme_light_surfaceTint),
        visualTransformation = VisualTransformation.None,
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(if (isSystemInDarkTheme()) md_theme_dark_surfaceVariant else md_theme_light_surfaceVariant)
                    .border(
                        width = .4.dp,
                        shape = RoundedCornerShape(4.dp),
                        color = Color.LightGray
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    innerTextField()
                }
            }
        }
    )
}