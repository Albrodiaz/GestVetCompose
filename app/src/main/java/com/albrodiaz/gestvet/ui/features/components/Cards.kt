package com.albrodiaz.gestvet.ui.features.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.albrodiaz.gestvet.ui.theme.*

@Composable
fun DetailCard(content: @Composable ()-> Unit) {
    val containerColor = if (isSystemInDarkTheme()) md_theme_dark_surface else md_theme_light_inverseOnSurface
    Card(
        modifier = Modifier.padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = Shapes.large
    ) {
        content()
    }
}