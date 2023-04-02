package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
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
import com.albrodiaz.gestvet.ui.features.components.*
import com.albrodiaz.gestvet.ui.features.home.viewmodels.pets.DetailPetViewModel

@Composable
fun PetDetailScreen(
    detailPetViewModel: DetailPetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isEditActive by detailPetViewModel.editEnabled.collectAsState()
    val showConfirmDialog by detailPetViewModel.showDialog.collectAsState()

    ConfirmDeleteDialog(
        title = stringResource(id = R.string.confirmDelete),
        text = stringResource(id = R.string.deleteDescription),
        show = showConfirmDialog,
        onDismiss = { detailPetViewModel.setShowDialog(false) },
        onConfirm = {
            detailPetViewModel.deletePet()
            onNavigateBack()
        }
    )

    Column(Modifier.fillMaxSize()) {
        DetailsTopBar(
            title = stringResource(id = R.string.pet),
            editEnabled = isEditActive,
            onDelete = { detailPetViewModel.setShowDialog(true) },
            onEdit = {
                detailPetViewModel.setEdit(!isEditActive)
                if (isEditActive) {
                    detailPetViewModel.updateData()
                    savedToast(context)
                }
            },
            onNavigateBack = { onNavigateBack() }
        )
        PetDetailSection(detailPetViewModel, enabled = isEditActive)
        Divider(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        MonitoringText(modifier = Modifier.fillMaxWidth())
        /*TODO: Lista con seguimiento de la mascota*/

    }
}

@Composable
fun PetDetailSection(detailPetViewModel: DetailPetViewModel, enabled: Boolean) {
    detailPetViewModel.apply {
        val name by petName.collectAsState()
        val birthDate by petBirth.collectAsState()
        val breed by petBreed.collectAsState()
        val color by petColor.collectAsState()
        val chipNumber by petChip.collectAsState()
        val passport by petPassport.collectAsState()
        val neutered by petNeutered.collectAsState()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            DetailTextRow(
                descriptionText = stringResource(id = R.string.name),
                enabled = enabled,
                text = name
            ) {
                setPetName(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.birthDate),
                enabled = enabled,
                text = birthDate
            ) {
                setPetBirth(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.breed),
                enabled = enabled,
                text = breed
            ) {
                setPetBreed(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.color),
                enabled = enabled,
                text = color
            ) {
                setPetColor(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.chipNumber),
                enabled = enabled,
                text = chipNumber
            ) {
                setPetChip(it)
            }
            DetailTextRow(
                descriptionText = stringResource(id = R.string.passportNumber),
                enabled = enabled,
                text = passport
            ) {
                setPetPassport(it)
            }
            NeuteredRow(
                neutered = neutered,
                enabled = enabled,
                onCheckChange = { setPetNeutered(it) })
        }
    }
}


@Composable
private fun DetailTextRow(
    enabled: Boolean,
    descriptionText: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        PetDetailText(text = descriptionText)
        SmallTextField(
            value = text,
            enabled = enabled,
            valueChange = { onValueChange(it) }
        )
    }
}

@Composable
private fun NeuteredRow(neutered: Boolean, enabled: Boolean, onCheckChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        PetDetailText(text = stringResource(id = R.string.neutered))
        NeuteredSwitch(
            neutered = neutered,
            enabled = enabled,
            onCheckedChange = { onCheckChange(it) })
    }
}

@Composable
private fun PetDetailText(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(.4f),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            fontSize = 14.sp,
            maxLines = 1
        )
    }
}

@Composable
fun MonitoringText(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.monitoring),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(9.dp)
    )
}