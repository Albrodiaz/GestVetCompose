package com.albrodiaz.gestvet.ui.features.home.views.appointments

import android.util.Log
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
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val lazyListState = rememberLazyListState()

    AddAppointmentDialog(show = showDialog, appointmentViewModel = appointmentViewModel)


    ConstraintLayout(Modifier.fillMaxSize()) {
        val (addButton) = createRefs()
        LazyColumn(
            state = lazyListState, modifier = Modifier
                .fillMaxSize()
        ) {
            items(items = appointments, key = { it.id ?: -1 }) {
                Box(modifier = Modifier
                    .clickable { Log.i("alberto", "Item: ${it.id}") }
                    .animateItemPlacement(tween(1000))) {
                    ItemAppointment(
                        appointment = it,
                        modifier = Modifier
                    ) { appointmentViewModel.showDeleteDialog(true) }
                }
                ConfirmDeleteDialog(
                    show = showDeleteDialog,
                    onDismiss = { appointmentViewModel.showDeleteDialog(false) },
                    onConfirm = {
                        Log.i("alberto", "${it.owner}")
                        appointmentViewModel.showDeleteDialog(false)
                    })
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
fun ItemAppointment(
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
                    orientation = Orientation.Horizontal,
                )
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .padding(6.dp),
            elevation = CardDefaults.cardElevation(3.dp),
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
fun ItemContent(appointment: AppointmentModel, modifier: Modifier) {
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
        CardDivider(modifier = Modifier.constrainAs(divider) {
            start.linkTo(date.end)
        })
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
fun DateTextField(text: String, modifier: Modifier) {
    Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = modifier)
}

@Composable
fun AppointmentTextField(text: String, modifier: Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier.padding(start = 6.dp)
    )
}

@Composable
fun CardDivider(modifier: Modifier) {
    Divider(
        modifier
            .fillMaxHeight()
            .padding(6.dp)
            .width(1.dp)
    )
}