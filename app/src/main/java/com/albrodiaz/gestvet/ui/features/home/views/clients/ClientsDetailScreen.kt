package com.albrodiaz.gestvet.ui.features.home.views.clients

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
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
import com.albrodiaz.gestvet.ui.theme.*

@Composable
fun ClientDetailScreen(
    navigationController: NavHostController,
    clientsDetailsViewModel: ClientDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isEditActive by clientsDetailsViewModel.isEditActive.collectAsState()
    val seniorityText by clientsDetailsViewModel.clientSeniority.collectAsState()

    Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
        ClientHeader(
            onClose = { navigationController.popBackStack() },
            onEdit = {
                clientsDetailsViewModel.enableEdit(!isEditActive)
                if (isEditActive) {
                    clientsDetailsViewModel.saveChanges()
                    savedToast(context)
                }
            },
            enabled = isEditActive
        )
        Row(Modifier.fillMaxWidth()) {
            DetailsDescriptionText()
            ClientSection(clientsDetailsViewModel = clientsDetailsViewModel)
        }
        ActionsSection(seniorityText = seniorityText)
        Divider(modifier = Modifier.padding(horizontal = 18.dp))
        PetSection()
    }
}

@Composable
private fun PetSection() {
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
                .height(250.dp)
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
fun ClientHeader(onClose: () -> Unit, onEdit: () -> Unit, enabled: Boolean) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        val (title, edit, close) = createRefs()
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
            onClick = { onClose() },
            modifier = Modifier.constrainAs(close) {
                end.linkTo(parent.end)
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
            }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
        }
        IconButton(onClick = { onEdit() },
            modifier = Modifier.constrainAs(edit) {
                top.linkTo(close.top)
                end.linkTo(close.start)
            }) {
            Icon(
                imageVector = if (!enabled) Icons.Filled.Edit else Icons.Filled.Done,
                contentDescription = null
            )
        }
    }
}

@Composable
fun DetailsDescriptionText() {
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
    /*TODO: buscar forma de extraer estados*/
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
            ClientTextfield(
                text = nameText,
                enabled = isEditActive,
                valueChange = { setName(it) })
            ClientTextfield(
                text = lastNameText,
                enabled = isEditActive,
                valueChange = { setLastName(it) })
            ClientTextfield(
                text = addressText,
                enabled = isEditActive,
                valueChange = { setAddress(it) })
            ClientTextfield(
                text = emailText,
                enabled = isEditActive,
                valueChange = { setEmail(it) })
            ClientTextfield(
                text = phoneText,
                enabled = isEditActive,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                valueChange = { setPhone(it) })
            ClientTextfield(
                text = clientIdText,
                enabled = isEditActive,
                valueChange = { setClientId(it) }
            )
        }
    }
}

@Composable
fun ActionsSection(seniorityText: Long) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${stringResource(id = R.string.since)} ${seniorityText.toDate()}", fontSize = 14.sp)
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.delete))
        }
    }
}

@Composable
private fun ClientTextfield(
    text: String,
    modifier: Modifier = Modifier,
    valueChange: (String) -> Unit,
    enabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        value = text,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        onValueChange = { valueChange(it) },
        singleLine = true,
        enabled = enabled,
        maxLines = 1,
        textStyle = TextStyle(
            color = if (isSystemInDarkTheme()) md_theme_dark_onSurface else md_theme_light_onSurface
        ),
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(md_theme_light_surfaceTint),
        visualTransformation = VisualTransformation.None,
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(if (isSystemInDarkTheme()) md_theme_dark_surfaceVariant else md_theme_light_surfaceVariant)
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = text,
            modifier = modifier.padding(vertical = 12.dp, horizontal = 6.dp)
        )
    }
}

private fun savedToast(context: Context) {
    Toast.makeText(context, R.string.savedChanges, Toast.LENGTH_SHORT).show()
}