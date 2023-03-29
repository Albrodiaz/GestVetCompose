package com.albrodiaz.gestvet.ui.features.home.views.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.EmptySearch
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.search.SearchViewModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.search.searchBy
import com.albrodiaz.gestvet.ui.theme.*

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    navigateApptDetails: (Long) -> Unit,
    navigateClientDetail: (Long) -> Unit,
    navigatePetDetail: (Long) -> Unit
) {
    val userText by searchViewModel.userText.collectAsState()
    val appointments by searchViewModel.appointments.collectAsState(initial = emptyList())
    val clients by searchViewModel.clients.collectAsState(initial = emptyList())
    val pets by searchViewModel.pets.collectAsState(initial = emptyList())

    val filteredList = (appointments + clients + pets).searchBy(userText)

    Column(Modifier.fillMaxSize()) {

        Row {
            CustomSearchTextField(
                value = userText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                valueChange = { searchViewModel.setUserText(it) }
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
                items(filteredList) { item ->
                    ItemSearchScreen(
                        item = item,
                        navigateApptDetails = { navigateApptDetails(it) },
                        navigateClientDetail = { navigateClientDetail(it) },
                        navigatePetDetail = { navigatePetDetail(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CustomSearchTextField(
    value: String,
    modifier: Modifier,
    valueChange: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth(.9f)
            .height(45.dp)
            .padding(horizontal = 16.dp)
            .onFocusChanged {
                if (it.isFocused) valueChange("") else valueChange("Buscar")
            },
        onValueChange = {
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
private fun ItemSearchScreen(
    item: Any,
    navigateApptDetails: (Long) -> Unit,
    navigateClientDetail: (Long) -> Unit,
    navigatePetDetail: (Long) -> Unit
) {
    val color = if (isSystemInDarkTheme()) md_theme_dark_onSurface else md_theme_light_onSurface
    when (item) {
        is AppointmentModel -> {
            ListItem(
                modifier = Modifier.clickable { navigateApptDetails(item.id ?: -1) },
                headlineContent = { Text(text = item.owner.toString()) },
                supportingContent = { Text(text = item.pet.toString()) },
                trailingContent = { Text(text = item.date.toString()) },
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
                modifier = Modifier.clickable { navigateClientDetail(item.id) },
                headlineContent = { Text(text = item.name.toString()) },
                supportingContent = { Text(text = item.lastname.toString()) },
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
                modifier = Modifier.clickable { navigatePetDetail(item.id ?: -1) },
                headlineContent = { Text(text = item.name.toString()) },
                supportingContent = { Text(text = item.breed.toString()) },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pets),
                        contentDescription = "",
                        tint = color
                    )
                },
                shadowElevation = 4.dp
            )
        }
    }
}