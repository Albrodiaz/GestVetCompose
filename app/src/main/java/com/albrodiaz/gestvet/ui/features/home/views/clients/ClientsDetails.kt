package com.albrodiaz.gestvet.ui.features.home.views.clients

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.theme.client_textfield_background
import com.albrodiaz.gestvet.ui.theme.md_theme_light_surfaceTint

@Composable
fun ClientDetailScreen(navigationController: NavHostController) {
    Column(Modifier.fillMaxSize()) {
        ClientSection(navigationController = navigationController)
        PetSection()
    }
}

@Composable
private fun ClientSection(navigationController: NavHostController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (divider, title, close, name, lastname, address, email, phone, id,
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
            modifier = Modifier.constrainAs(id) {
                top.linkTo(phone.bottom)
                start.linkTo(phone.start)
            }
        )
        ClientDetailText(text = "${stringResource(id = R.string.since)} 12/03/2023",
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(seniority) {
                    top.linkTo(id.bottom)
                    start.linkTo(parent.start)
                })
        ClientTextfield(
            text = "Nombre",
            modifier = Modifier.constrainAs(nameInput) {
                top.linkTo(name.top)
                end.linkTo(parent.end)
                bottom.linkTo(name.bottom)
            }, valueChange = { })
        ClientTextfield(
            text = "Apellidos",
            modifier = Modifier.constrainAs(lastnameInput) {
                top.linkTo(lastname.top)
                end.linkTo(parent.end)
                bottom.linkTo(lastname.bottom)

            }, valueChange = { })
        ClientTextfield(
            text = "Dirección",
            modifier = Modifier.constrainAs(addressInput) {
                top.linkTo(address.top)
                end.linkTo(parent.end)
                bottom.linkTo(address.bottom)
            }, valueChange = { })
        ClientTextfield(
            text = "Email",
            modifier = Modifier.constrainAs(emailInput) {
                top.linkTo(email.top)
                end.linkTo(parent.end)
                bottom.linkTo(email.bottom)
            }, valueChange = { })
        ClientTextfield(
            text = "Teléfono",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.constrainAs(phoneInput) {
                top.linkTo(phone.top)
                end.linkTo(parent.end)
                bottom.linkTo(phone.bottom)
            }, valueChange = { })
        ClientTextfield(
            text = "DNI",
            modifier = Modifier.constrainAs(idInput) {
                top.linkTo(id.top)
                end.linkTo(parent.end)
                bottom.linkTo(id.bottom)
            },
            valueChange = {  }
        )
        Divider(
            modifier = Modifier
                .background(client_textfield_background)
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                },
            thickness = .3.dp,
        )
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        value = text,
        modifier = modifier
            .fillMaxWidth(.7f)
            .height(45.dp),
        onValueChange = { valueChange(it) },
        singleLine = true,
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