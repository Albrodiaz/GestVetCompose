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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.ui.features.home.models.ClientsModel
import com.albrodiaz.gestvet.ui.theme.client_textfield_background
import com.albrodiaz.gestvet.ui.theme.md_theme_light_surfaceTint

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ClientDetailScreen() {
    Column(Modifier.fillMaxSize()) {
        ClientSection()
        PetSection()
    }
}

@Composable
private fun ClientSection() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (title, close, name, lastname, address, email, phone, nameInput, lastnameInput, addressInput, emailInput, phoneInput, seniority) = createRefs()
        Text(
            text = "Cliente",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(close) {
            end.linkTo(parent.end)
            top.linkTo(title.top)
            bottom.linkTo(title.bottom)
        }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "")
        }
        ClientDetailText(text = "Nombre", modifier = Modifier.constrainAs(name) {
            top.linkTo(title.bottom)
            start.linkTo(parent.start)
        })
        ClientDetailText(text = "Apellidos", modifier = Modifier.constrainAs(lastname) {
            top.linkTo(name.bottom)
            start.linkTo(name.start)
        })
        ClientDetailText(text = "Dirección", modifier = Modifier.constrainAs(address) {
            top.linkTo(lastname.bottom)
            start.linkTo(lastname.start)
        })
        ClientDetailText(text = "Email", modifier = Modifier.constrainAs(email) {
            top.linkTo(address.bottom)
            start.linkTo(address.start)
        })
        ClientDetailText(text = "Teléfono", modifier = Modifier.constrainAs(phone) {
            top.linkTo(email.bottom)
            start.linkTo(email.start)
        })
        ClientTextfield(modifier = Modifier.constrainAs(nameInput) {
            top.linkTo(name.top)
            end.linkTo(parent.end)
            bottom.linkTo(name.bottom)
        }, valueChange = { })
        ClientTextfield(modifier = Modifier.constrainAs(lastnameInput) {
            top.linkTo(lastname.top)
            end.linkTo(parent.end)
            bottom.linkTo(lastname.bottom)

        }, valueChange = { })
        ClientTextfield(modifier = Modifier.constrainAs(addressInput) {
            top.linkTo(address.top)
            end.linkTo(parent.end)
            bottom.linkTo(address.bottom)
        }, valueChange = { })
        ClientTextfield(modifier = Modifier.constrainAs(emailInput) {
            top.linkTo(email.top)
            end.linkTo(parent.end)
            bottom.linkTo(email.bottom)
        }, valueChange = { })
        ClientTextfield(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.constrainAs(phoneInput) {
                top.linkTo(phone.top)
                end.linkTo(parent.end)
                bottom.linkTo(phone.bottom)
            }, valueChange = { })
        ClientDetailText(text = "Cliente desde: ", modifier = Modifier.fillMaxWidth().constrainAs(seniority) {
            top.linkTo(phone.bottom)
            start.linkTo(parent.start)
        })
    }
}

@Composable
private fun PetSection() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Mascotas",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Añadir")
            }
        }
        /*TODO: Borrar cuando tenga lista de clientes y crear stringResources*/
        val clients = listOf(
            ClientsModel(
                name = "Alberto",
                lastname = "Rodríguez Díaz",
                phoneNumber = "666777888"
            ), ClientsModel(name = "Paloma", lastname = "Genescá Gómez", phoneNumber = "666888777")
        )
        LazyColumn(
            modifier = Modifier
                .padding(bottom = 12.dp)
        ) {
            items(clients + clients + clients) {
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
    modifier: Modifier,
    valueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var text: String by remember { mutableStateOf("") }

    BasicTextField(
        value = text,
        modifier = modifier
            .fillMaxWidth(.7f)
            .height(45.dp),
        onValueChange = {
            text = it
            valueChange(it)
        },
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
            modifier = modifier.padding(vertical = 14.dp, horizontal = 6.dp),
            fontSize = 14.sp
        )
    }
}