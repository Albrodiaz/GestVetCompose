package com.albrodiaz.gestvet.ui.features.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTextField(
    value: String,
    placeholder: String,
    isError: Boolean = false,
    supportingText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    valueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = valueChange,
        placeholder = { Text(text = placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        maxLines = 1,
        singleLine = true,
        isError = isError,
        supportingText = { Text(text = supportingText) },
        shape = Shapes.medium,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPassword(
    value: String,
    placeholder: String = stringResource(id = R.string.password),
    isError: Boolean = false,
    supportingText: String = "",
    valueChange: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val icon = if (!visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    TextField(
        value = value,
        onValueChange = valueChange,
        placeholder = { Text(text = placeholder) },
        isError = isError,
        supportingText = { Text(text = supportingText) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        maxLines = 1,
        singleLine = true,
        shape = Shapes.medium,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(imageVector = icon, contentDescription = "Toggle password")
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, text: String, enabled: Boolean, doLogin: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = doLogin,
        shape = Shapes.medium,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                fontSize = 16.sp
            )
        )
    }
}