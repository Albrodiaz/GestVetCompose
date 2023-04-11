package com.albrodiaz.gestvet.ui.features.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.theme.*

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
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            cursorColor = md_theme_light_primary,
            focusedLabelColor = md_theme_light_primary
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ElevatedTextField(
    value: String,
    valueChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocus = LocalFocusManager.current

    Surface(
        shadowElevation = 6.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        shape = Shapes.large
    ) {
        TextField(
            value = value,
            onValueChange = { valueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.search)) },
            maxLines = 1,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    keyboardController?.hide()
                    localFocus.clearFocus(true)
                }
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                localFocus.clearFocus(true)
            })
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTextField(
    value: String,
    valueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor = if (isSystemInDarkTheme()) md_theme_dark_onSurface else md_theme_light_onSurface
    BasicTextField(
        value = value,
        onValueChange = { valueChange(it) },
        enabled = enabled,
        textStyle = TextStyle(
            color = textColor,
            fontFamily = Montserrat,
            fontSize = 12.sp
        ),
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(),
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            shape = Shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                top = 6.dp,
                bottom = 6.dp,
            ),
        )
    }
}