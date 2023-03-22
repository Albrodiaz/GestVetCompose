package com.albrodiaz.gestvet.ui.features.home.views.clients

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.toDate
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.clients.ClientDetailsViewModel
import com.albrodiaz.gestvet.ui.theme.client_textfield_background
import com.albrodiaz.gestvet.ui.theme.md_theme_light_surfaceTint

@Composable
fun ClientDetailScreen(navigationController: NavHostController) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        ClientSection(
            clientsDetailsViewModel = hiltViewModel(),
            context = context,
            navigationController = navigationController
        )
        Divider(
            modifier = Modifier
                .background(client_textfield_background),
        )
        PetSection()
    }
}

@Composable
private fun ClientSection(
    clientsDetailsViewModel: ClientDetailsViewModel,
    context: Context,
    navigationController: NavHostController
) {
    clientsDetailsViewModel.apply {
        val nameText by name.collectAsState()
        val lastNameText by lastName.collectAsState()
        val addressText by address.collectAsState()
        val emailText by email.collectAsState()
        val phoneText by phone.collectAsState()
        val clientIdText by clientId.collectAsState()
        val seniorityText by clientSeniority.collectAsState()
        val isEditActive by isEditActive.collectAsState()

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val (title, close, edit, name, lastname, address, email, phone, clientId,
                nameInput, lastnameInput, addressInput, emailInput,
                phoneInput, seniority, idInput) = createRefs()

            Text(
                text = stringResource(id = R.string.client),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    })
            IconButton(
                onClick = { navigationController.navigateUp() },
                modifier = Modifier.constrainAs(close) {
                    end.linkTo(parent.end)
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }
            IconButton(onClick = {
                enableEdit(!isEditActive)
                if (isEditActive) {
                    saveChanges()
                    savedToast(context)
                }
            }, modifier = Modifier.constrainAs(edit) {
                top.linkTo(close.top)
                end.linkTo(close.start)
            }) {
                Icon(
                    imageVector = if (!isEditActive) Icons.Filled.Edit else Icons.Filled.Done,
                    contentDescription = null
                )
            }
            ClientDetailText(
                text = stringResource(id = R.string.name),
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                })
            ClientDetailText(
                text = stringResource(id = R.string.lastName),
                modifier = Modifier.constrainAs(lastname) {
                    top.linkTo(name.bottom)
                    start.linkTo(name.start)
                })
            ClientDetailText(
                text = stringResource(id = R.string.address),
                modifier = Modifier.constrainAs(address) {
                    top.linkTo(lastname.bottom)
                    start.linkTo(lastname.start)
                })
            ClientDetailText(
                text = stringResource(id = R.string.email),
                modifier = Modifier.constrainAs(email) {
                    top.linkTo(address.bottom)
                    start.linkTo(address.start)
                })
            ClientDetailText(
                text = stringResource(id = R.string.phone),
                modifier = Modifier.constrainAs(phone) {
                    top.linkTo(email.bottom)
                    start.linkTo(email.start)
                })
            ClientDetailText(
                text = stringResource(id = R.string.id),
                modifier = Modifier.constrainAs(clientId) {
                    top.linkTo(phone.bottom)
                    start.linkTo(phone.start)
                }
            )
            ClientTextfield(
                text = nameText,
                enabled = isEditActive,
                modifier = Modifier.constrainAs(nameInput) {
                    top.linkTo(name.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(name.bottom)
                }, valueChange = { setName(it) })
            ClientTextfield(
                text = lastNameText,
                enabled = isEditActive,
                modifier = Modifier.constrainAs(lastnameInput) {
                    top.linkTo(lastname.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(lastname.bottom)

                }, valueChange = { setLastName(it) })
            ClientTextfield(
                text = addressText,
                enabled = isEditActive,
                modifier = Modifier.constrainAs(addressInput) {
                    top.linkTo(address.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(address.bottom)
                }, valueChange = { setAddress(it) })
            ClientTextfield(
                text = emailText,
                enabled = isEditActive,
                modifier = Modifier.constrainAs(emailInput) {
                    top.linkTo(email.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(email.bottom)
                }, valueChange = { setEmail(it) })
            ClientTextfield(
                text = phoneText,
                enabled = isEditActive,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.constrainAs(phoneInput) {
                    top.linkTo(phone.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(phone.bottom)
                }, valueChange = { setPhone(it) })
            ClientTextfield(
                text = clientIdText,
                enabled = isEditActive,
                modifier = Modifier.constrainAs(idInput) {
                    top.linkTo(clientId.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(clientId.bottom)
                },
                valueChange = { setClientId(it) }
            )
            ClientDetailText(text = "${stringResource(id = R.string.since)} ${seniorityText.toDate()}",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(seniority) {
                        top.linkTo(clientId.bottom)
                        start.linkTo(parent.start)
                    })
        }
    }
}

@Composable
private fun PetSection() {
    Column(
        Modifier
            .fillMaxSize()
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
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.add))
            }
        }
        /*TODO: Borrar cuando tenga lista de mascotas*/
        val clients = listOf(
            ClientsModel(
                id = 1,
                name = "Alberto",
                lastname = "Rodríguez Díaz",
                phoneNumber = "666777888"
            ),
            ClientsModel(
                id = 2,
                name = "Paloma",
                lastname = "Genescá Gómez",
                phoneNumber = "666888777"
            )
        )
        LazyColumn(
            modifier = Modifier
                .padding(bottom = 12.dp)
        ) {
            items(items = clients, key = { it.id }) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    shadowElevation = 2.dp,
                    supportingText = { Text(text = "Algo") },
                    headlineText = { Text(text = "Marley") }
                )
            }
        }
    }
}

@Composable
private fun ClientTextfield(
    text: String,
    modifier: Modifier,
    valueChange: (String) -> Unit,
    enabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        value = text,
        modifier = modifier
            .fillMaxWidth(.7f)
            .height(45.dp),
        onValueChange = { valueChange(it) },
        singleLine = true,
        enabled = enabled,
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(md_theme_light_surfaceTint),
        visualTransformation = VisualTransformation.None,
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(client_textfield_background)
                    .border(
                        width = .4.dp,
                        shape = RoundedCornerShape(4.dp),
                        color = Color.LightGray
                    ),
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
                }
            }
        }
    )
}

@Composable
private fun ClientDetailText(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(.3f)) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 14.dp, horizontal = 6.dp)
        )
    }
}

private fun savedToast(context: Context) {
    Toast.makeText(context, R.string.savedChanges, Toast.LENGTH_SHORT).show()
}