package com.example.prviprojekatrma.breeds.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.prviprojekatrma.breeds.list.BreedListContract.BreedListUiEvent
import com.example.prviprojekatrma.breeds.list.BreedListContract.BreedListState
import com.example.prviprojekatrma.breeds.list.model.BreedUiModel
import com.example.prviprojekatrma.core.theme.EnableEdgeToEdge
import kotlinx.coroutines.launch
import com.example.prviprojekatrma.drawer.AppDrawer
import com.example.prviprojekatrma.drawer.AppDrawerDestination

fun NavGraphBuilder.breeds(
    route: String,
    onUserClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onBreedsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) = composable(
    route = route
) {
    val breedListViewModel = hiltViewModel<BreedListViewModel>()

    val state = breedListViewModel.state.collectAsState()
    EnableEdgeToEdge(isDarkTheme = false)

    BreedsListScreen(
        state = state.value,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onUserClick = onUserClick,
        onProfileClick = onProfileClick,
        onBreedsClick = onBreedsClick,
        onQuizClick = onQuizClick,
        onLeaderboardClick = onLeaderboardClick
    )
}

@Composable
fun BreedsListScreen(
    eventPublisher: (uiEvent: BreedListUiEvent) -> Unit,
    state: BreedListState,
    onUserClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onBreedsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    val uiScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                drawerState = drawerState,
                onDrawerDestinationClick = { destination ->
                    when (destination) {
                        AppDrawerDestination.Profile -> onProfileClick()
                        AppDrawerDestination.Breeds -> onBreedsClick()
                        AppDrawerDestination.Quiz -> onQuizClick()
                        AppDrawerDestination.Leaderboard -> onLeaderboardClick()
                    }
                }
            )
        },
        content = {
            BreedsListScaffold(
                eventPublisher = eventPublisher,
                state = state,
                onUserClick = onUserClick,
                onDrawerMenuClick = {
                    uiScope.launch { drawerState.open() }
                }
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BreedsListScaffold(
    eventPublisher: (uiEvent: BreedListUiEvent) -> Unit,
    state: BreedListState,
    onUserClick: (String) -> Unit,
    onDrawerMenuClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onDrawerMenuClick) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                title = { Text(text = "Breeds List") },
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = state.query,
                    onValueChange = {
                        eventPublisher(BreedListUiEvent.SearchQueryChanged(it))
                    },
                    label = { Text("Search") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            IconButton(onClick = {
                                eventPublisher(BreedListUiEvent.ClearSearch)
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear Search"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                if (state.error != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is BreedListContract.ListError.ListUpdateFailed ->
                                "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = if (state.isSearchMode) state.filteredUsers else state.breeds,
                            key = { it.id },
                        ) { breedUiModel ->
                            BreedListItem(breed = breedUiModel) { onUserClick(breedUiModel.id) }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BreedListItem(
    breed: BreedUiModel,
    onUserClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onUserClick(breed.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = breed.name, style = MaterialTheme.typography.titleMedium)
            breed.alt_names?.let {
                Text(text = "Alternative names: $it", style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "Description: ${breed.description.shortenDescription()}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Temperament: ${breed.temperament.pickThreeTemperaments()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun String.shortenDescription(maxLength: Int = 250): String {
    if (this.length <= maxLength) return this
    var lastPeriodIndex = this.lastIndexOf('.', startIndex = maxLength - 1)
    if (lastPeriodIndex == -1 || lastPeriodIndex < (maxLength - 50)) {
        lastPeriodIndex = maxLength
    }
    return this.substring(0, lastPeriodIndex + 1)
}

fun String.pickThreeTemperaments(): String {
    val temperaments = this.split(',').map { it.trim() }
    return temperaments.take(3).joinToString(", ")
}
