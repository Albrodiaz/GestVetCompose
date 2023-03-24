package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.components.AddTopBar
import com.albrodiaz.gestvet.ui.features.components.FormTextField
import com.albrodiaz.gestvet.ui.features.home.viewmodels.clients.AddClientViewModel


@Composable
fun AddClientScreen(
    addClientViewModel: AddClientViewModel,
    onNavigateUp: () -> Unit
) {
    val clientName by addClientViewModel.clientName.collectAsState(initial = "")
    val clientLastName by addClientViewModel.clientLastName.collectAsState(initial = "")
    val clientAddress by addClientViewModel.clientAddress.collectAsState(initial = "")
    val clientPhone by addClientViewModel.clientPhone.collectAsState(initial = "")
    val clientEmail by addClientViewModel.clientEmail.collectAsState(initial = "")
    val clientId by addClientViewModel.clientId.collectAsState("")
    val isSaveEnabled by addClientViewModel.isSaveEnabled.collectAsState(initial = false)

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (topBar, content) = createRefs()
        AddTopBar(
            title = stringResource(id = R.string.addClient),
            modifier = Modifier.constrainAs(topBar) {
                top.linkTo(parent.top)
                bottom.linkTo(content.top)
            }
        ) {
            onNavigateUp()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .constrainAs(content) {
                    top.linkTo(topBar.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormTextField(
                text = clientName,
                modifier = Modifier.fillMaxWidth(),
                textChange = { addClientViewModel.setName(it) },
                placeholder = "${stringResource(id = R.string.name)} *",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            FormTextField(
                text = clientLastName,
                modifier = Modifier.fillMaxWidth(),
                textChange = { addClientViewModel.setLastName(it) },
                placeholder = "${stringResource(R.string.lastName)} *",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            FormTextField(
                text = clientAddress,
                modifier = Modifier.fillMaxWidth(),
                textChange = { addClientViewModel.setAddress(it) },
                placeholder = "${stringResource(id = R.string.address)} (${stringResource(id = R.string.optional)})",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            FormTextField(
                text = clientPhone,
                modifier = Modifier.fillMaxWidth(),
                textChange = { addClientViewModel.setPhone(it) },
                placeholder = "${stringResource(id = R.string.phone)} *",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            FormTextField(
                text = clientEmail,
                modifier = Modifier.fillMaxWidth(),
                textChange = { addClientViewModel.setEmail(it) },
                placeholder = "${stringResource(id = R.string.email)} (${stringResource(id = R.string.optional)})",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            FormTextField(
                text = clientId ?: "",
                modifier = Modifier.fillMaxWidth(),
                textChange = { addClientViewModel.setId(it) },
                placeholder = "${stringResource(id = R.string.id)} (${stringResource(id = R.string.optional)})",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                enabled = isSaveEnabled,
                onClick = {
                    addClientViewModel.saveClient()
                    onNavigateUp()
                }) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}