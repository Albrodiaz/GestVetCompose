package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.ui.features.components.ConfirmDeleteDialog
import com.albrodiaz.gestvet.ui.features.components.DetailsTextfield
import com.albrodiaz.gestvet.ui.features.components.DetailsTopBar
import com.albrodiaz.gestvet.ui.features.components.savedToast
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.clients.ClientDetailsViewModel

@Composable
fun ClientDetailScreen(
    clientsDetailsViewModel: ClientDetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    navigateToDetails: (Long) -> Unit,
    onCreatePet: (Long) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isEditActive by clientsDetailsViewModel.isEditActive.collectAsState()
    val seniorityText by clientsDetailsViewModel.clientSeniority.collectAsState()
    val showDeleteDialog by clientsDetailsViewModel.showDialog.collectAsState()
    val showWarningDialog by clientsDetailsViewModel.showWarning.collectAsState()
    val pets by clientsDetailsViewModel.pets.collectAsState(emptyList())

    ConfirmDeleteDialog(
        title = stringResource(id = R.string.confirmDeleteClient),
        text = stringResource(id = R.string.deleteDescription),
        show = showDeleteDialog,
        onDismiss = { clientsDetailsViewModel.setShowDialog(false) }
    ) {
        clientsDetailsViewModel.setShowWarning(true)
        clientsDetailsViewModel.setShowDialog(false)
    }

    WarningDeleteDialog(
        title = stringResource(id = R.string.warning),
        text = stringResource(id = R.string.deleteClientDesc),
        show = showWarningDialog,
        onDismiss = { clientsDetailsViewModel.setShowWarning(false) }
    ) {
        clientsDetailsViewModel.deleteClient()
        onNavigateUp()
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        ClientHeader(
            onClose = { onNavigateUp() },
            onEdit = {
                clientsDetailsViewModel.enableEdit(!isEditActive)
                if (isEditActive) {
                    clientsDetailsViewModel.saveChanges()
                    savedToast(context)
                }
            },
            onDelete = { clientsDetailsViewModel.setShowDialog(true) },
            enabled = isEditActive
        )
        Row(Modifier.fillMaxWidth()) {
            DetailsDescriptionText()
            ClientSection(clientsDetailsViewModel = clientsDetailsViewModel)
        }
        SenioritySection(seniorityText = seniorityText)
        Divider(modifier = Modifier.padding(horizontal = 18.dp))
        PetSection(pets = pets, navigateToDetails = { navigateToDetails(it) }) {
            onCreatePet(clientsDetailsViewModel.ownerId ?: 0L)
        }
    }
}

@Composable
private fun ClientHeader(
    onClose: () -> Unit,
    onEdit: () -> Unit,
    enabled: Boolean,
    onDelete: () -> Unit
) {
    DetailsTopBar(
        title = stringResource(id = R.string.client),
        editEnabled = enabled,
        onDelete = { onDelete() },
        onEdit = { onEdit() },
        onNavigateBack = { onClose() }
    )
}

@Composable
private fun DetailsDescriptionText() {
    Column(Modifier.fillMaxWidth(.3f)) {
        ClientDetailText(text = stringResource(id = R.string.name))
        ClientDetailText(text = stringResource(id = R.string.lastName))
        ClientDetailText(text = stringResource(id = R.string.address))
        ClientDetailText(text = stringResource(id = R.string.email))
        ClientDetailText(text = stringResource(id = R.string.phone))
        ClientDetailText(text = stringResource(id = R.string.id))
    }
}

@Composable
private fun ClientSection(
    clientsDetailsViewModel: ClientDetailsViewModel
) {
    clientsDetailsViewModel.apply {
        val nameText by name.collectAsState()
        val lastNameText by lastName.collectAsState()
        val addressText by address.collectAsState()
        val emailText by email.collectAsState()
        val phoneText by phone.collectAsState()
        val clientIdText by clientId.collectAsState()
        val isEditActive by isEditActive.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            DetailsTextfield(
                text = nameText,
                enabled = isEditActive,
                valueChange = { setName(it) })
            DetailsTextfield(
                text = lastNameText,
                enabled = isEditActive,
                valueChange = { setLastName(it) })
            DetailsTextfield(
                text = addressText,
                enabled = isEditActive,
                valueChange = { setAddress(it) })
            DetailsTextfield(
                text = emailText,
                enabled = isEditActive,
                valueChange = { setEmail(it) })
            DetailsTextfield(
                text = phoneText,
                enabled = isEditActive,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                valueChange = { setPhone(it) })
            DetailsTextfield(
                text = clientIdText,
                enabled = isEditActive,
                valueChange = { setClientId(it) }
            )
        }
    }
}

@Composable
private fun SenioritySection(seniorityText: Long) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${stringResource(id = R.string.since)} ${seniorityText.toDate()}",
            fontSize = 14.sp
        )
    }
}

@Composable
private fun PetSection(
    pets: List<PetModel>,
    navigateToDetails: (Long) -> Unit,
    onNavigate: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.pets),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            TextButton(onClick = { onNavigate() }) {
                Text(text = stringResource(id = R.string.add))
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .height(250.dp)
        ) {
            items(pets, key = { it.id ?: 0L }) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable { navigateToDetails(it.id!!) },
                    shadowElevation = 2.dp,
                    supportingContent = { Text(text = "${it.breed}") },
                    headlineContent = { Text(text = "${it.name}") }
                )
            }
        }
    }
}

@Composable
private fun ClientDetailText(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
private fun WarningDeleteDialog(
    title: String,
    text: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmDeleteDialog(
        title = title,
        text = text,
        show = show,
        onDismiss = { onDismiss() }) {
        onConfirm()
    }
}