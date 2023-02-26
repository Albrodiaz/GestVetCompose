package com.albrodiaz.gestvet.ui.views.features.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.data.AppointmentProvider.Companion.appointments
import com.albrodiaz.gestvet.ui.theme.*
import com.albrodiaz.gestvet.ui.views.features.appointments.ItemAppointment

@Preview
@Composable
fun SearchScreen() {
    //TODO: crear layout para items y buscar mejor forma de filtrar

    ConstraintLayout(Modifier.fillMaxSize()) {
        var userText: String by remember { mutableStateOf("") }
        val filteredList = appointments.filter {
            it.owner.lowercase().contains(userText.lowercase()) ||
                    it.pet.lowercase().contains(userText.lowercase())
        }
        val (searchBar, content) = createRefs()
        CustomSearchTextField(modifier = Modifier
            .padding(top = 12.dp)
            .constrainAs(searchBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }, valueChange = { userText = it })
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .constrainAs(content) {
                    top.linkTo(searchBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }) {
            items(filteredList) {
                ItemAppointment(appointment = it)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomSearchTextField(modifier: Modifier, valueChange: (String) -> Unit) {
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
                            keyboardController?.hide()
                        },
                        enabled = true
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Search dog",
                            tint = md_theme_light_onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    )
}

/*
private fun searchFilter(filterText: String): List<AppointmentModel> {
    val filteredList = appointments.filter {
        it.owner.lowercase().contains(filterText.lowercase()) ||
                it.pet.lowercase().contains(filterText.lowercase())
    }
    return filteredList
}*/
