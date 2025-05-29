package com.example.prviprojekatrma.gallery

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter

fun NavGraphBuilder.gallery(
    route: String,
    navController: NavController
) = composable(
    route = route,
    arguments = listOf(
        navArgument(name = "breeds_id") {
            nullable = false
            type = NavType.StringType
        }
    )
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("breeds_id")
        ?: throw IllegalArgumentException("breeds_id is required.")

    val galleryViewModel: GalleryViewModel = hiltViewModel()
    val state by galleryViewModel.state.collectAsState()

    GalleryScreen(
        state = state,
        onClose = {
            navController.navigateUp()
        },
        onLoadMore = {
            galleryViewModel.setEvent(GalleryContract.GalleryUiEvent.LoadNextPage)
        },
        onPreviousPage = {
            galleryViewModel.setEvent(GalleryContract.GalleryUiEvent.LoadPreviousPage)
        },
        onNextPage = {
            galleryViewModel.setEvent(GalleryContract.GalleryUiEvent.LoadNextPage)
        },
        onImageClicked = { index ->
            galleryViewModel.setEvent(GalleryContract.GalleryUiEvent.ImageClicked(index))
            navController.navigate("breeds/photo_viewer/$breedId/$index")
        },
        currentPage = galleryViewModel.currentPage,
        totalPages = galleryViewModel.totalPages,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    state: GalleryContract.GalleryState,
    onClose: () -> Unit,
    onLoadMore: () -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onImageClicked: (Int) -> Unit,
    currentPage: Int,
    totalPages: Int,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Gallery") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BoxWithConstraints(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    val screenWidth = this.maxWidth
                    val cellSize = (screenWidth / 2) - 8.dp

                    if (state.loading && state.images.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (state.error != null && state.images.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${state.error}")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.padding(8.dp),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.images) { imageUrl ->
                                val index = state.images.indexOf(imageUrl)
                                Image(
                                    painter = rememberImagePainter(
                                        data = imageUrl,
                                        builder = {
                                            placeholder(android.R.drawable.ic_menu_gallery)
                                            error(android.R.drawable.ic_menu_report_image)
                                        }
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(cellSize)
                                        .clickable { onImageClicked(index) }
                                )
                            }
                            if (!state.loading && state.images.isNotEmpty()) {
                                item {
                                    LaunchedEffect(Unit) {
                                        onLoadMore()
                                    }
                                }
                            }
                        }
                        if (state.loading && state.images.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                PaginationControls(
                    onPrevious = onPreviousPage,
                    onNext = onNextPage,
                    currentPage = currentPage,
                    totalPages = totalPages
                )
            }
        }
    )
}

@Composable
fun PaginationControls(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    currentPage: Int,
    totalPages: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPrevious, enabled = currentPage > 1) {
            Text("Previous")
        }
        Text("Page $currentPage of $totalPages")
        Button(onClick = onNext, enabled = currentPage < totalPages) {
            Text("Next")
        }
    }
}
