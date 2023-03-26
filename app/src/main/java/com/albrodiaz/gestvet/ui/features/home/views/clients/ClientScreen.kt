package com.albrodiaz.gestvet.ui.features.home.views.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.core.extensions.isScrolled
import com.albrodiaz.gestvet.ui.features.components.AnimatedAddFab
import com.albrodiaz.gestvet.ui.features.components.EmptyContent
import com.albrodiaz.gestvet.ui.features.home.viewmodels.clients.ClientViewModel

@Composable
fun ClientScreen(
    clientViewModel: ClientViewModel,
    navigateToCreate: () -> Unit,
    navigateToDetails: (Long) -> Unit
) {
    val clients by clientViewModel.clients.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (button, emptyScreen) = createRefs()
            if (clients.isEmpty()) {
                EmptyContent()
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().constrainAs(emptyScreen) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    items(clients, key = { it.id }) {
                        ListItem(
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                            shadowElevation = 4.dp,
                            supportingContent = { Text(text = it.lastname.toString()) },
                            headlineContent = { Text(text = it.name.toString()) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = ""
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = { navigateToDetails(it.id) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_arrow_right),
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                    }
                }
            }
            AnimatedAddFab(
                modifier = Modifier.constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                visible = listState.isScrolled
            ) {
                navigateToCreate()
            }
        }
    }
}