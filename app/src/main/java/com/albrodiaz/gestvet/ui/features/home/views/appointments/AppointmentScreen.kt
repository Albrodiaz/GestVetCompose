package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.core.extensions.isScrolled
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel
import com.albrodiaz.gestvet.ui.theme.*
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppointmentScreen(appointmentViewModel: AppointmentViewModel) {

    val appointments by appointmentViewModel.appointments.collectAsState(initial = emptyList())
    val showDialog by appointmentViewModel.visibleDialog.observeAsState(initial = false)
    val showDeleteDialog by appointmentViewModel.visibleDeleteDialog.observeAsState(false)
    val selectedAppointment by appointmentViewModel.selectedAppointment.observeAsState()
    val lazyListState = rememberLazyListState()

    AddAppointmentDialog(show = showDialog, appointmentViewModel = appointmentViewModel)
    ConfirmDeleteDialog(
        show = showDeleteDialog,
        onDismiss = { appointmentViewModel.showDeleteDialog(false) }) {
        appointmentViewModel.apply {
            selectedAppointment?.let {
                deleteAppointment(it)
            }
            showDeleteDialog(false)
        }
    }


    ConstraintLayout(Modifier.fillMaxSize()) {
        val (addButton) = createRefs()
        LazyColumn(
            state = lazyListState, modifier = Modifier
                .fillMaxSize()
        ) {
            items(items = appointments, key = { it.id ?: -1 }) { appointment ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement(tween(1000))
                ) {
                    ItemAppointment(
                        appointment = appointment,
                        modifier = Modifier
                    ) {
                        appointmentViewModel.apply {
                            setSelectedAppointment(appointment)
                            showDeleteDialog(true)
                        }
                    }
                }
            }
        }
        AnimatedAddFab(
            visible = lazyListState.isScrolled,
            modifier = Modifier.constrainAs(addButton) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }) {
            appointmentViewModel.showDialog(true)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemAppointment(
    appointment: AppointmentModel,
    modifier: Modifier,
    showDeleteDialog: () -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val buttonZIndex = animateFloatAsState(targetValue = swipeableState.progress.fraction)
    val width = 75.dp
    val sizePx = with(LocalDensity.current) { width.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    ConstraintLayout(modifier) {
        val (button) = createRefs()
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(120.dp)
                .zIndex(1f)
                .swipeable(
                    swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .padding(6.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(containerColor = appointment_card_container)
        ) {
            ItemContent(appointment = appointment, modifier = Modifier)
        }
        DeleteButton(
            swipeableState = swipeableState,
            modifier = Modifier
                .padding(end = 12.dp)
                .zIndex(buttonZIndex.value)
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }) { showDeleteDialog() }
    }
}

@Composable
private fun ItemContent(appointment: AppointmentModel, modifier: Modifier) {
    ConstraintLayout(modifier.fillMaxSize()) {
        val (date, hour, divider, owner, pet, subject) = createRefs()
        DateTextField(
            text = "${appointment.date}",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 16.dp, bottom = 8.dp)
                .constrainAs(date) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(hour.top)
                }
        )
        DateTextField(
            text = "${appointment.hour}",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .constrainAs(hour) {
                    top.linkTo(date.bottom)
                    start.linkTo(date.start)
                    end.linkTo(date.end)
                })
        Divider(
            modifier
                .fillMaxHeight()
                .padding(6.dp)
                .width(1.dp)
                .constrainAs(divider) { start.linkTo(date.end) }
        )

        AppointmentTextField(text = "${appointment.owner}", modifier = Modifier
            .padding(top = 12.dp)
            .constrainAs(owner) {
                start.linkTo(divider.end)
                top.linkTo(parent.top)
            })
        AppointmentTextField(
            text = "${appointment.pet}",
            modifier = Modifier
                .padding(top = 12.dp)
                .constrainAs(pet) {
                    top.linkTo(owner.bottom)
                    start.linkTo(owner.start)
                })
        AppointmentTextField(
            text = "${appointment.subject}",
            modifier = Modifier
                .padding(top = 12.dp)
                .constrainAs(subject) {
                    top.linkTo(pet.bottom)
                    start.linkTo(owner.start)
                })
    }
}

@Composable
private fun DateTextField(text: String, modifier: Modifier) {
    Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = modifier)
}

@Composable
private fun AppointmentTextField(text: String, modifier: Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier.padding(start = 6.dp)
    )
}

@Composable
private fun ConfirmDeleteDialog(show: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "¿Eliminar la cita?") },
            text = { Text(text = "Una vez eliminada no se podrá recuperar.") },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Cancelar")
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddAppointmentDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false
            )
        ) {
            androidx.compose.material.Surface(
                Modifier
                    .fillMaxSize()
                    .background(md_theme_light_surface)
            ) {
                content()
            }
        }
    }
}

@Composable
fun AnimatedAddFab(modifier: Modifier, visible: Boolean, showDialog: () -> Unit) {
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { with(density) { 60.dp.roundToPx() } },
            exit = slideOutVertically { with(density) { 100.dp.roundToPx() } }
        ) {
            FloatingActionButton(modifier = modifier.size(45.dp), onClick = { showDialog() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DeleteButton(
    modifier: Modifier,
    swipeableState: SwipeableState<Int>? = null,
    onDeleteAppointment: () -> Unit
) {
    val density = LocalDensity.current
    Box(modifier = modifier.fillMaxHeight()) {
        AnimatedVisibility(
            visible = swipeableState?.targetValue == 1 && swipeableState.progress.fraction > 0.5,
            enter = fadeIn(animationSpec = tween(1500)) + slideInHorizontally { with(density) { 100.dp.roundToPx() } },
            exit = fadeOut(animationSpec = tween(1500)) + slideOutHorizontally { with(density) { 50.dp.roundToPx() } }
        ) {
            FloatingActionButton(
                onClick = { onDeleteAppointment() },
                containerColor = md_theme_light_error
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "", tint = Color.White)
            }
        }
    }
}