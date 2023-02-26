package com.albrodiaz.gestvet.ui.views.features.appointments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.albrodiaz.gestvet.ui.theme.md_theme_light_error
import kotlin.math.roundToInt

@Composable
fun AppointmentScreen(paddingValue: Dp) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValue)
    ) {
        items(10) {
            ItemAppointment()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ItemAppointment() {
    val swipeableState = rememberSwipeableState(initialValue = 0) //0 colapsado, 1 expandido
    val width = 75.dp
    val sizePx = with(LocalDensity.current) { width.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (container, button) = createRefs()
        ElevatedCard(
            modifier = Modifier
                .constrainAs(container) {}
                .fillMaxWidth()
                .height(135.dp)
                .zIndex(if (swipeableState.targetValue == 0) 1f else 0f)
                .swipeable(
                    swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal,
                )
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }

                .padding(6.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            AppointmentContainer()
        }
        DeleteButton(
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            swipeableState = swipeableState
        )
    }
}

@Composable
fun AppointmentContainer() {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
    ) {
        val (date, hour, divider, owner, pet, subject) = createRefs()

        Text(text = "Fecha", modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .constrainAs(date) {
                top.linkTo(parent.top)
                end.linkTo(divider.start)
                start.linkTo(parent.start)
            })
        Text(text = "Hora", modifier = Modifier
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
            text = "Propietario",
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .constrainAs(owner) { start.linkTo(divider.end) })
        Text(
            text = "Mascota",
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .constrainAs(pet) {
                    top.linkTo(owner.bottom)
                    start.linkTo(owner.start)
                })
        Text(
            text = "Asunto",
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .constrainAs(subject) {
                    top.linkTo(pet.bottom)
                    start.linkTo(pet.start)
                })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteButton(modifier: Modifier, swipeableState: SwipeableState<Int>) {
    Box(
        modifier = modifier
            .height(40.dp)
            .padding(end = 16.dp)
    ) {
        AnimatedVisibility(
            visible = swipeableState.targetValue == 1 && swipeableState.progress.fraction > 0.6,
            enter = fadeIn(animationSpec = tween(800)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            FloatingActionButton(
                onClick = { /*Borrar item*/ },
                containerColor = md_theme_light_error
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "", tint = Color.White)
            }
        }
    }

}