package com.example.prviprojekatrma.gallery

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter

fun NavGraphBuilder.photoViewer(
    route: String,
    navController: NavController
) = composable(
    route = route,
    arguments = listOf(
        navArgument(name = "breeds_id") {
            nullable = false
            type = NavType.StringType
        },
        navArgument(name = "image_index") {
            nullable = false
            type = NavType.IntType
        }
    )
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("breeds_id")
        ?: throw IllegalArgumentException("breeds_id is required.")
    val imageIndex = navBackStackEntry.arguments?.getInt("image_index")
        ?: throw IllegalArgumentException("image_index is required.")

    val galleryViewModel: GalleryViewModel = hiltViewModel()
    val state by galleryViewModel.state.collectAsState()

    PhotoViewerScreen(
        state = state,
        breedId = breedId,
        initialIndex = imageIndex,
        onClose = {
            navController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PhotoViewerScreen(
    state: GalleryContract.GalleryState,
    breedId: String,
    initialIndex: Int,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { state.images.size }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Photo Viewer") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.images.isEmpty() && state.loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.error != null && state.images.isEmpty()) {
                    Text("Error: ${state.error}", modifier = Modifier.align(Alignment.Center))
                } else {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = rememberImagePainter(
                                data = state.images[page],
                                builder = {
                                    placeholder(android.R.drawable.ic_menu_gallery)
                                    error(android.R.drawable.ic_menu_report_image)
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    )
}
