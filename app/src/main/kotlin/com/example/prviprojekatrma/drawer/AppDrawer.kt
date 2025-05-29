package com.example.prviprojekatrma.drawer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.example.prviprojekatrma.R

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    onDrawerDestinationClick: (AppDrawerDestination) -> Unit,
) {
    val uiScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<AppDrawerViewModel>()

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    val uiState = viewModel.state.collectAsState()

    AppDrawerContent(
        state = uiState.value,
        onDrawerDestinationClick = {
            uiScope.launch { drawerState.close() }
            onDrawerDestinationClick(it)
        },
        eventPublisher = {
            // TODO Publish event
        },
    )
}

@Composable
fun AppDrawerContent(
    state: AppDrawerContract.UiState,
    eventPublisher: (AppDrawerContract.UiEvent) -> Unit,
    onDrawerDestinationClick: (AppDrawerDestination) -> Unit,
) {
    Surface {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .navigationBarsPadding()
                .width(300.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
        ) {
            AppDrawerActionItem(
                icon = R.drawable.user,
                text = "Profile",
                onClick = { onDrawerDestinationClick(AppDrawerDestination.Profile) },
            )
            AppDrawerActionItem(
                icon = R.drawable.toygercat,
                text = "Breeds",
                onClick = { onDrawerDestinationClick(AppDrawerDestination.Breeds) },
            )
            AppDrawerActionItem(
                icon = R.drawable.quiz,
                text = "Quiz",
                onClick = { onDrawerDestinationClick(AppDrawerDestination.Quiz) },
            )
            AppDrawerActionItem(
                icon = R.drawable.competition,
                text = "Leaderboard",
                onClick = { onDrawerDestinationClick(AppDrawerDestination.Leaderboard) },
            )
        }
    }
}

@Composable
fun AppDrawerActionItem(
    icon: Int,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier.clickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() }
        ),
        leadingContent = {
            Icon(painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp))

        },
        headlineContent = {
            Text(text = text)
        }
    )
}
