package com.albrodiaz.gestvet.ui.features.home.views.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.ui.features.components.ElevatedTextField
import com.albrodiaz.gestvet.ui.features.components.EmptySearch
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.search.SearchViewModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.search.searchBy

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

        ElevatedTextField(value = userText) {
            searchViewModel.setUserText(it)
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

@Composable
private fun ItemSearchScreen(
    item: Any,
    navigateApptDetails: (Long) -> Unit,
    navigateClientDetail: (Long) -> Unit,
    navigatePetDetail: (Long) -> Unit
) {
    when (item) {
        is AppointmentModel -> {
            SearchItem(
                headLineText = item.owner,
                supportingText = item.pet,
                trailingText = item.apptDate!!.time.toDate(),
                leadingIcon = Icons.Filled.DateRange,
                navigateDetails = { navigateApptDetails(item.id ?: -1) })
        }
        is ClientsModel -> {
            SearchItem(
                headLineText = item.name,
                supportingText = item.lastname,
                leadingIcon = Icons.Filled.Person,
                navigateDetails = { navigateClientDetail(item.id) }
            )
        }
        is PetModel -> {
            SearchItem(
                headLineText = item.name,
                supportingText = item.breed,
                leadingIcon = Icons.Default.Pets,
                navigateDetails = { navigatePetDetail(item.id ?: -1) }
            )
        }
    }
}

@Composable
fun SearchItem(
    headLineText: String? = null,
    supportingText: String? = null,
    trailingText: String? = null,
    leadingIcon: ImageVector,
    navigateDetails: () -> Unit
) {
    Card(
        modifier = Modifier.padding(6.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        ListItem(
            modifier = Modifier
                .clickable { navigateDetails() },
            headlineContent = { Text(text = headLineText ?: "") },
            supportingContent = { Text(text = supportingText ?: "") },
            trailingContent = { Text(text = trailingText ?: "") },
            leadingContent = { Icon(imageVector = leadingIcon, contentDescription = null) },
            shadowElevation = 3.dp
        )
    }

}