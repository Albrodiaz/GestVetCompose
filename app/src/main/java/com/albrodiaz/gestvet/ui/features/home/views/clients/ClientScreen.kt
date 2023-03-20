package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.isScrolled
import com.albrodiaz.gestvet.ui.features.home.models.Routes
import com.albrodiaz.gestvet.ui.features.home.viewmodels.ClientViewModel
import com.albrodiaz.gestvet.ui.features.home.views.appointments.AnimatedAddFab

@Composable
fun ClientScreen(clientViewModel: ClientViewModel, navigationController: NavHostController) {

    val clients by clientViewModel.clients.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (button) = createRefs()
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(clients, key = { it.id }) {
                    ListItem(
                        shadowElevation = 4.dp,
                        supportingText = { Text(text = it.lastname.toString()) },
                        headlineText = { Text(text = it.name.toString()) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = ""
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { navigationController.navigate(Routes.ClientDetails.route) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_right),
                                    contentDescription = ""
                                )
                            }
                        }
                    )
                }
            }
            AnimatedAddFab(
                modifier = Modifier.constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                visible = listState.isScrolled
            ) {
                navigationController.navigate(Routes.AddClient.route)
            }
        }
    }
}