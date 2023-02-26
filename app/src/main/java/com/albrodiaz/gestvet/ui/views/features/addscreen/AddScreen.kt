package com.albrodiaz.gestvet.ui.views.features.addscreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddScreen() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var ownerText: String by remember { mutableStateOf("") }
    var petText: String by remember { mutableStateOf("") }
    var dateText: String by remember { mutableStateOf("") }
    var hourText: String by remember { mutableStateOf("") }
    var subjectText: String by remember { mutableStateOf("") }
    var detailText: String by remember { mutableStateOf("") }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (title, owner, pet, date, hour, subject, detail, saveButton) = createRefs()

        AddScreenTittle(modifier = Modifier
            .padding(start = 24.dp)
            .constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
        FormTextField(
            text = ownerText,
            label = stringResource(id = R.string.owner),
            modifier = Modifier.constrainAs(owner) {
                top.linkTo(title.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textChange = { ownerText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        FormTextField(
            text = petText,
            label = stringResource(id = R.string.pet),
            modifier = Modifier.constrainAs(pet) {
                top.linkTo(owner.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textChange = { petText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        FormTextField(text = dateText,
            label = stringResource(id = R.string.date),
            modifier = Modifier
                .fillMaxWidth(.35f)
                .constrainAs(date) {
                    top.linkTo(pet.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(hour.start)
                },
            textChange = { dateText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        FormTextField(text = hourText,
            label = stringResource(id = R.string.hour),
            modifier = Modifier
                .fillMaxWidth(.35f)
                .constrainAs(hour) {
                    top.linkTo(pet.bottom)
                    start.linkTo(date.end)
                    end.linkTo(parent.end)
                },
            textChange = { hourText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        FormTextField(
            text = subjectText,
            label = stringResource(id = R.string.subject),
            modifier = Modifier.constrainAs(subject) {
                top.linkTo(date.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textChange = { subjectText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        FormTextField(
            text = detailText,
            label = stringResource(id = R.string.details),
            modifier = Modifier.constrainAs(detail) {
                top.linkTo(subject.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textChange = { detailText = it },
            maxLines = 15,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Button(
            onClick = { showToast(context) },
            modifier = Modifier
                .padding(vertical = 12.dp)
                .constrainAs(saveButton) {
                    top.linkTo(detail.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            Text(text = stringResource(id = R.string.save))
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    text: String,
    label: String = "",
    modifier: Modifier,
    textChange: (String) -> Unit,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(.8f)
            .padding(vertical = 6.dp),
        value = text,
        label = { Text(text = label) },
        onValueChange = { textChange(it) },
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun AddScreenTittle(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.addTitle),
        modifier = modifier.padding(vertical = 16.dp, horizontal = 12.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    )
}

//TODO Borrar esta función cuando incluya base de datos
private fun showToast(context: Context) {
    Toast.makeText(context, "Próximamente", Toast.LENGTH_SHORT).show()
}