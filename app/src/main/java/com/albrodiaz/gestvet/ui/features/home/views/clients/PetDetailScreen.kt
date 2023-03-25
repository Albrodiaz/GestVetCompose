package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.DetailsTextfield
import com.albrodiaz.gestvet.ui.features.components.DetailsTopBar
import com.albrodiaz.gestvet.ui.features.home.viewmodels.pets.DetailPetViewModel

@Composable
fun PetDetailScreen(detailPetViewModel: DetailPetViewModel = hiltViewModel()) {
    val editEnabled by detailPetViewModel.editEnabled.collectAsState()
    Column(Modifier.fillMaxSize()) {
        DetailsTopBar(
            title = stringResource(id = R.string.pet),
            editEnabled = false,
            onDelete = { /*TODO: Borrar con id por parámetro*/ },
            onEdit = { detailPetViewModel.setEdit(!editEnabled) }) {
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            PetDetailSection()
            PetTextFieldSection(detailPetViewModel)
        }
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
fun PetDetailSection() {
    Column(Modifier.fillMaxWidth(.4f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        PetDetailText(text = stringResource(id = R.string.name))
        PetDetailText(text = stringResource(id = R.string.birthDate))
        PetDetailText(text = stringResource(id = R.string.breed))
        PetDetailText(text = stringResource(id = R.string.color))
        PetDetailText(text = stringResource(id = R.string.chipNumber))
        PetDetailText(text = stringResource(id = R.string.passportNumber))
        PetDetailText(text = stringResource(id = R.string.neutered))
    }
}

@Composable
fun PetTextFieldSection(detailPetViewModel: DetailPetViewModel) {
    detailPetViewModel.apply {
        val editEnabled by editEnabled.collectAsState()
        val petName by petName.collectAsState()
        val petBirth by petBirth.collectAsState()
        val petBreed by petBreed.collectAsState()
        val petColor by petColor.collectAsState()
        val petChip by petChip.collectAsState()
        val petPassport by petPassport.collectAsState()
        val petNeutered by petNeutered.collectAsState()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            DetailsTextfield(
                text = petName,
                enabled = editEnabled,
                valueChange = { setPetName(it) })
            DetailsTextfield(
                text = petBirth,
                enabled = editEnabled,
                valueChange = { setPetBirth(it) })
            DetailsTextfield(
                text = petBreed,
                enabled = editEnabled,
                valueChange = { setPetBreed(it) })
            DetailsTextfield(
                text = petColor,
                enabled = editEnabled,
                valueChange = { setPetColor(it) })
            DetailsTextfield(
                text = petChip,
                enabled = editEnabled,
                valueChange = { setPetChip(it) })
            DetailsTextfield(
                text = petPassport,
                enabled = editEnabled,
                valueChange = { setPetPassport(it) })
            DetailsTextfield(
                text = petNeutered,
                enabled = editEnabled,
                valueChange = { setPetNeutered(it) })
        }
    }
}

@Composable
private fun PetDetailText(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 12.dp, horizontal = 6.dp)
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