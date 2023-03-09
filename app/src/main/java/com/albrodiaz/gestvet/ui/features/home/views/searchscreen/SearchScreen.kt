package com.albrodiaz.gestvet.ui.features.home.views.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.core.extensions.searchBy
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel
import com.albrodiaz.gestvet.ui.theme.*

@Composable
fun SearchScreen(appointmentViewModel: AppointmentViewModel) {
    var userText: String by remember { mutableStateOf("") }
    val appointments by appointmentViewModel.appointments.collectAsState(initial = emptyList())
    /*
    TODO:
       crear switch para alternar entre citas y clientes o crear enum con tipo de item
    */
    ConstraintLayout(Modifier.fillMaxSize()) {
        val filteredList = appointments.searchBy(userText)
        val (searchBar, content) = createRefs()

        CustomSearchTextField(
            modifier = Modifier
                .padding(top = 12.dp)
                .constrainAs(searchBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            valueChange = { userText = it })

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .constrainAs(content) {
                    top.linkTo(searchBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }) {
            items(filteredList, key = { it.id ?: -1 }) {
                ItemSearchScreen(appointment = it)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CustomSearchTextField(modifier: Modifier, valueChange: (String) -> Unit) {
    val focus = LocalFocusManager.current
    var searchText: String by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = searchText,
        modifier = modifier
            .fillMaxWidth(.9f)
            .height(45.dp)
            .padding(horizontal = 16.dp)
            .onFocusChanged {
                searchText = if (it.isFocused) "" else "Buscar"
            },
        onValueChange = {
            searchText = it
            valueChange(it)
        },
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(md_theme_light_onSurface),
        visualTransformation = VisualTransformation.None,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            focus.clearFocus()
        }),
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp)
                    .background(md_theme_light_surfaceVariant, RoundedCornerShape(16.dp)),
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
                    IconButton(
                        onClick = {
                            focus.clearFocus()
                            keyboardController?.hide()
                        },
                        enabled = true
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Search",
                            tint = md_theme_light_onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemSearchScreen(appointment: AppointmentModel) {
    ListItem(
        headlineText = { Text(text = appointment.owner ?: "") },
        supportingText = { Text(text = appointment.pet ?: "") },
        trailingContent = { Text(text = appointment.date ?: "") },
        leadingContent = { Icon(Icons.Filled.DateRange, contentDescription = "") },
        shadowElevation = 4.dp
    )
}