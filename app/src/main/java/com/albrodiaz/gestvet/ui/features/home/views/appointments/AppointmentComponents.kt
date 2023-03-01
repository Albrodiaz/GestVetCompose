package com.albrodiaz.gestvet.ui.features.home.views.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.albrodiaz.gestvet.ui.theme.md_theme_light_error

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteButton(modifier: Modifier, swipeableState: SwipeableState<Int>? = null) {
    Box(modifier = modifier.fillMaxHeight()) {
        AnimatedVisibility(
            visible = swipeableState?.targetValue == 1 && swipeableState.progress.fraction > 0.5,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            FloatingActionButton(
                onClick = { /*TODO: Borrar item*/ },
                containerColor = md_theme_light_error
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "", tint = Color.White)
            }
        }
    }
}

@Composable
fun AppointmentCard(modifier: Modifier, content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(135.dp)
            .padding(6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        content()
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
            exit = slideOutVertically { with(density) { 60.dp.roundToPx() } }
        ) {
            FloatingActionButton(modifier = modifier.size(45.dp), onClick = { showDialog() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    }
}