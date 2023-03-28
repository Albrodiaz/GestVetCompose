package com.albrodiaz.gestvet.ui.features.home.views.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Check
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.ui.features.components.EmptySearch
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.search.SearchViewModel
import com.albrodiaz.gestvet.ui.theme.*

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {
    var userText: String by remember { mutableStateOf("") }
    val appointments by searchViewModel.appointments.collectAsState(initial = emptyList())
    val clients by searchViewModel.clients.collectAsState(initial = emptyList())
    val pets by searchViewModel.pets.collectAsState(initial = emptyList())

    val filteredList = (appointments + clients + pets).searchBy(userText)

    Column(Modifier.fillMaxSize()) {

        Row {
            CustomSearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                valueChange = { userText = it }
            )
        }
        if (filteredList.isEmpty()) {
            EmptySearch()
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp)
            ) {
                items(filteredList) {
                    ItemSearchScreen(it)
                }
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

@Composable
private fun ItemSearchScreen(item: Any) {
    val color = if (isSystemInDarkTheme()) md_theme_dark_onSurface else md_theme_light_onSurface
    when (item) {
        is AppointmentModel -> {
            ListItem(
                headlineContent = { Text(text = item.owner!!) },
                supportingContent = { Text(text = item.pet!!) },
                trailingContent = { Text(text = item.date!!) },
                leadingContent = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "",
                        tint = color
                    )
                },
                shadowElevation = 4.dp
            )
        }
        is ClientsModel -> {
            ListItem(
                headlineContent = { Text(text = item.name!!) },
                supportingContent = { Text(text = item.lastname!!) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "",
                        tint = color
                    )
                },
                shadowElevation = 4.dp
            )
        }
        is PetModel -> {
            ListItem(
                headlineContent = { Text(text = item.name!!) },
                supportingContent = { Text(text = item.breed!!) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "",
                        tint = color
                    )
                },
                shadowElevation = 4.dp
            )
        }
    }
}

fun List<Any>.searchBy(text: String): List<Any> {
    val filteredList = this.filter {
        when (it) {
            is AppointmentModel-> {
                it.owner!!.lowercase().contains(text.lowercase()) ||
                        it.pet!!.lowercase().contains(text.lowercase())
            }
            is ClientsModel -> {
                it.name!!.lowercase().contains(text.lowercase()) ||
                        it.lastname!!.lowercase().contains(text.lowercase())
            }
            is PetModel -> {
                it.name!!.lowercase().contains(text.lowercase()) ||
                        it.breed!!.lowercase().contains(text.lowercase())
            }
            else -> false
        }

    }
    return if (text.isEmpty()) emptyList() else filteredList
}