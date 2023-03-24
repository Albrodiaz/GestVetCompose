package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.ui.features.components.FormTextField
import com.albrodiaz.gestvet.ui.features.home.viewmodels.clients.AddClientViewModel
import com.albrodiaz.gestvet.ui.theme.GestVetTheme


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

    GestVetTheme {

        ConstraintLayout(Modifier.fillMaxSize()) {
            val (title, content, close) = createRefs()
            IconButton(
                onClick = {
                    onNavigateUp()
                },
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 6.dp)
                    .constrainAs(close) {
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                        end.linkTo(parent.end)
                    },
            ) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }
            Text(
                text = "Añadir cliente",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 24.dp)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .constrainAs(content) {
                        top.linkTo(title.bottom)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FormTextField(
                    text = clientName,
                    modifier = Modifier.fillMaxWidth(),
                    textChange = { addClientViewModel.setName(it) },
                    placeholder = "Nombre *",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                FormTextField(
                    text = clientLastName,
                    modifier = Modifier.fillMaxWidth(),
                    textChange = { addClientViewModel.setLastName(it) },
                    placeholder = "Apellidos *",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                FormTextField(
                    text = clientAddress,
                    modifier = Modifier.fillMaxWidth(),
                    textChange = { addClientViewModel.setAddress(it) },
                    placeholder = "Dirección (Opcional)",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                FormTextField(
                    text = clientPhone,
                    modifier = Modifier.fillMaxWidth(),
                    textChange = { addClientViewModel.setPhone(it) },
                    placeholder = "Teléfono *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                FormTextField(
                    text = clientEmail,
                    modifier = Modifier.fillMaxWidth(),
                    textChange = { addClientViewModel.setEmail(it) },
                    placeholder = "Email (Opcioinal)",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
                )
                FormTextField(
                    text = clientId ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textChange = { addClientViewModel.setId(it) },
                    placeholder = "NIF/CIF (Opcional)",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Button(
                    modifier = Modifier.padding(vertical = 24.dp),
                    enabled = isSaveEnabled,
                    onClick = {
                        addClientViewModel.saveClient()
                        onNavigateUp()
                    }) {
                    Text(text = "Guardar")
                }
            }
        }
    }
}