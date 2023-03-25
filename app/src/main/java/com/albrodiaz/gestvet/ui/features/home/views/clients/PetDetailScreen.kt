package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.DetailsTextfield
import com.albrodiaz.gestvet.ui.features.components.DetailsTopBar

@Preview
@Composable
fun PetDetailScreen() {
    Column(Modifier.fillMaxSize()) {
        DetailsTopBar(
            title = stringResource(id = R.string.pet),
            editEnabled = false,
            onDelete = { /*TODO: Borrar con id por parámetro*/ },
            onEdit = { /*TODO: igual que con clientes*/ }) {
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            PetDetailSection()
            PetTextFieldSection()
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
fun PetTextFieldSection() {
    /*TODO: Completar con stateflows*/
    Column(
        Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
    ) {
        DetailsTextfield(text = "Marley", valueChange = { })
        DetailsTextfield(text = "31/08/2023", valueChange = { })
        DetailsTextfield(text = "Beagle", valueChange = { })
        DetailsTextfield(text = "Marrón, blanco y negro", valueChange = { })
        DetailsTextfield(text = "123456789999", valueChange = { })
        DetailsTextfield(text = "234566556754754", valueChange = { })
        DetailsTextfield(text = "Si", valueChange = { })
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