package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.ui.features.components.*
import com.albrodiaz.gestvet.ui.features.home.models.PetModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.clients.ClientDetailsViewModel

@Composable
fun ClientDetailScreen(
    clientsDetailsViewModel: ClientDetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    navigateToDetails: (Long) -> Unit,
    onCreatePet: (Long) -> Unit
) {
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
            .verticalScroll(rememberScrollState())
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
        DetailCard {
            ClientSection(clientsDetailsViewModel = clientsDetailsViewModel)
            SenioritySection(seniorityText = seniorityText)
        }
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
                .padding(16.dp)
        ) {
            ClientDetailRow(
                enabled = isEditActive,
                descriptionText = stringResource(id = R.string.name),
                text = nameText,
                onValueChange = { clientsDetailsViewModel.setName(it) }
            )
            ClientDetailRow(
                enabled = isEditActive,
                descriptionText = stringResource(id = R.string.lastName),
                text = lastNameText,
                onValueChange = { clientsDetailsViewModel.setLastName(it) }
            )
            ClientDetailRow(
                enabled = isEditActive,
                descriptionText = stringResource(id = R.string.address),
                text = addressText,
                onValueChange = { clientsDetailsViewModel.setAddress(it) }
            )
            ClientDetailRow(
                enabled = isEditActive,
                descriptionText = stringResource(id = R.string.email),
                text = emailText,
                onValueChange = { clientsDetailsViewModel.setEmail(it) }
            )
            ClientDetailRow(
                enabled = isEditActive,
                descriptionText = stringResource(id = R.string.phone),
                text = phoneText,
                onValueChange = { clientsDetailsViewModel.setPhone(it) }
            )
            ClientDetailRow(
                enabled = isEditActive,
                descriptionText = stringResource(id = R.string.id),
                text = clientIdText,
                onValueChange = { clientsDetailsViewModel.setClientId(it) }
            )
        }
    }
}

@Composable
private fun SenioritySection(seniorityText: Long) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
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
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.pets),
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { onNavigate() }) {
                Text(text = stringResource(id = R.string.add))
            }
        }

        LazyVerticalGrid(
            modifier = Modifier
                .height(250.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(pets, key = { it.id ?: 0L }) {
                ItemPet(
                    headlineText = it.name.toString(),
                    supportingText = it.breed.toString()
                ) {
                    navigateToDetails(it.id ?: -1)
                }
            }
        }
    }
}

@Composable
private fun ClientDetailText(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(.3f)
    ) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 12.dp),
            fontSize = 14.sp
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

@Composable
private fun ItemPet(
    headlineText: String,
    supportingText: String,
    navigateToDetails: () -> Unit
) {
    Card(
        modifier = Modifier.padding(vertical = 6.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        ListItem(
            modifier = Modifier.clickable { navigateToDetails() },
            headlineContent = { Text(text = headlineText) },
            supportingContent = { Text(text = supportingText) }
        )
    }
}

@Composable
private fun ClientDetailRow(
    enabled: Boolean,
    descriptionText: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        ClientDetailText(text = descriptionText)
        SmallTextField(value = text, valueChange = { onValueChange(it) }, enabled = enabled)
    }
}