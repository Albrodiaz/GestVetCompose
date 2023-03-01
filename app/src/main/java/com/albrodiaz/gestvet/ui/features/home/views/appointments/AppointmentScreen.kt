package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.core.extensions.isScrolled
import com.albrodiaz.gestvet.data.AppointmentProvider.Companion.appointments
import com.albrodiaz.gestvet.ui.features.home.models.AppointmentModel
import kotlin.math.roundToInt

@Composable
fun AppointmentScreen() {
    val appointments = appointments //Borrar al crear viewmodel
    var showDialog: Boolean by remember { mutableStateOf(false) }

    if (showDialog) {
        AddAppointmentDialog(showDialog = { showDialog = !showDialog })
    } else {
        AppointmentScreenContent(appointments) { showDialog = true }
    }
}

@Composable
fun AppointmentScreenContent(appointments: List<AppointmentModel>, showDialog: () -> Unit) {
    val lazyListState = rememberLazyListState()
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (listItem, addButton) = createRefs()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(listItem, constrainBlock = {}),
            state = lazyListState
        ) {
            items(appointments) { appointment ->
                ItemAppointment(appointment)
            }
        }
        AnimatedAddFab(modifier = Modifier.constrainAs(addButton) {
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
        }, lazyListState.isScrolled) { showDialog() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemAppointment(appointment: AppointmentModel) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val buttonZIndex = animateFloatAsState(targetValue = swipeableState.progress.fraction)
    val width = 75.dp
    val sizePx = with(LocalDensity.current) { width.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (container, button) = createRefs()
        AppointmentCard(
            modifier = Modifier
                .constrainAs(container) {}
                .zIndex(1f)
                .swipeable(
                    swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal,
                )
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
        ) {
            AppointmentLayout(appointment = appointment)
        }
        DeleteButton(
            swipeableState = swipeableState,
            modifier = Modifier
                .height(40.dp)
                .padding(end = 16.dp)
                .zIndex(buttonZIndex.value)
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
fun AppointmentLayout(appointment: AppointmentModel) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (date, hour, divider, owner, pet, subject) = createRefs()

        Text(text = appointment.date,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 24.dp)
                .constrainAs(date) {
                    top.linkTo(parent.top)
                    end.linkTo(divider.start)
                    start.linkTo(parent.start)
                })
        Text(text = appointment.hour,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .constrainAs(hour) {
                    top.linkTo(date.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(divider.start)
                })
        Divider(
            Modifier
                .width(1.dp)
                .padding(vertical = 6.dp)
                .fillMaxHeight()
                .constrainAs(divider) {
                    start.linkTo(date.end)
                    end.linkTo(owner.start)
                })
        Text(
            fontSize = 15.sp,
            text = appointment.owner,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(owner) {
                    start.linkTo(divider.end)
                    bottom.linkTo(pet.top)
                    top.linkTo(parent.top)
                })
        Text(
            text = appointment.pet,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(pet) {
                    top.linkTo(owner.bottom)
                    start.linkTo(owner.start)
                    bottom.linkTo(subject.top)
                })
        Text(
            text = appointment.subject,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(subject) {
                    top.linkTo(pet.bottom)
                    start.linkTo(pet.start)
                    bottom.linkTo(parent.bottom)
                })
    }
}
