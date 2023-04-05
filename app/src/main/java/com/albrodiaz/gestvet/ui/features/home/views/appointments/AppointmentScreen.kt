package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.isScrolled
import com.albrodiaz.gestvet.ui.features.components.AnimatedAddFab
import com.albrodiaz.gestvet.ui.features.components.ConfirmDeleteDialog
import com.albrodiaz.gestvet.ui.features.components.EmptyContent
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import com.albrodiaz.gestvet.ui.features.home.viewmodels.appointments.AppointmentViewModel
import com.albrodiaz.gestvet.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun AppointmentScreen(
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
    onNavigate: (Long?) -> Unit
) {

    val appointments by appointmentViewModel.appointments.collectAsState(initial = emptyList())
    val showDeleteDialog by appointmentViewModel.visibleDeleteDialog.observeAsState(false)
    val selectedAppointment by appointmentViewModel.selectedAppointment.observeAsState()

    ConfirmDeleteDialog(
        show = showDeleteDialog,
        onDismiss = { appointmentViewModel.showDeleteDialog(false) },
        title = stringResource(id = R.string.confirmDelete),
        text = stringResource(id = R.string.deleteDescription)
    ) {
        appointmentViewModel.apply {
            selectedAppointment?.let {
                deleteAppointment(it.id?:-1)
            }
            showDeleteDialog(false)
        }
    }

    Appointments(
        appointments = appointments,
        appointmentViewModel = appointmentViewModel,
        onNavigate = { onNavigate(0L) }
    ) {
        onNavigate(it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Appointments(
    appointments: List<AppointmentModel>,
    appointmentViewModel: AppointmentViewModel,
    onNavigate: (Long?)->Unit,
    onItemSelected: (Long) -> Unit
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val state = rememberLazyListState()
        val (addButton, emptyAppointments) = createRefs()

        if (appointments.isEmpty()) {
            EmptyContent(modifier = Modifier.constrainAs(emptyAppointments) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        } else {
            LazyColumn(
                state = state, modifier = Modifier
                    .fillMaxSize()
            ) {
                items(items = appointments, key = { it.id ?: -1 }) { appointment ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement(tween(500))
                            .clickable { onItemSelected(appointment.id ?: 0L) }
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
        }

        AnimatedAddFab(
            visible = state.isScrolled,
            modifier = Modifier.constrainAs(addButton) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }) { onNavigate(0L) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemAppointment(
    appointment: AppointmentModel,
    modifier: Modifier,
    onDelete: () -> Unit
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
            elevation = CardDefaults.cardElevation(3.dp)
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
                }) { onDelete() }
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
            visible = (swipeableState?.targetValue == 1) && (swipeableState.progress.fraction > 0.5),
            enter = fadeIn(animationSpec = tween(1500)) + slideInHorizontally { with(density) { 100.dp.roundToPx() } },
            exit = fadeOut(animationSpec = tween(1500)) + slideOutHorizontally { with(density) { 80.dp.roundToPx() } }
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