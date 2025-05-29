
package com.example.prviprojekatrma.breedDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.prviprojekatrma.breedDetails.model.BreedDetailUiModel


fun NavGraphBuilder.breedDetails(
    route: String,
    navController: NavController,
) = composable(
    route = route,
    arguments = listOf(
        navArgument(name = "breeds_id") {
            nullable = false
            type = NavType.StringType
        }
    )
) { navBackStackEntry ->
    val breedDetailsViewModel: BreedDetailsViewModel = hiltViewModel()

    val state = breedDetailsViewModel.state.collectAsState().value

    BreedDetailsScreen(
        state = state,
        onClose = {
            navController.navigateUp()
        },
        onViewGallery = {
            navController.navigate("breeds/gallery/${state.breedId}")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(state: BreedDetailsState, onClose: () -> Unit,    onViewGallery: () -> Unit) {

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.breedDetail?.name ?: "", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { padding ->
        state.breedDetail?.let { currentBreed ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.breedImageURL?.let { url ->
                    Image(
                        painter = rememberImagePainter(data = url),
                        contentDescription = "Slika rase ${currentBreed.name}",
                        modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is BreedDetailsState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else {
                    BreedDetailsCard(currentBreed)
                    CharacteristicsRow(currentBreed)
                    WikipediaLinkButton(currentBreed.wikipedia_url, uriHandler)
                    Button(onClick = onViewGallery) {
                        Text("View Gallery")
                    }
                }

            }
        }
    }
}

@Composable
fun BreedDetailsCard(currentBreed: BreedDetailUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ime: ${currentBreed.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Opis: ${currentBreed.description}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Zemlje porekla: ${currentBreed.origin}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Osobine temperamenta: ${currentBreed.temperament}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Zivotni vek: ${currentBreed.life_span}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Prosecna tezina: ${currentBreed.weight}",
                modifier = Modifier.padding(top = 8.dp)
            )
            if(currentBreed.is_rare){
                Text(text = "Retka vrsta")
            } else {
                Text(text = "Nije retka vrsta")
            }
        }
    }
}

@Composable
fun CharacteristicsRow(currentBreed: BreedDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        DetailChip("Adaptability", currentBreed.adaptability)
        DetailChip("Energy Level", currentBreed.energy_level)
        DetailChip("Affection Level", currentBreed.affection_level)
        DetailChip("Child friendly", currentBreed.child_friendly)
        DetailChip("Dog friendly", currentBreed.dog_friendly)
    }
}

@Composable
fun WikipediaLinkButton(url: String?, uriHandler: UriHandler) {
    url?.let {
        Button(
            onClick = { uriHandler.openUri(url) },
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Info")
            Text("Pročitaj više na Vikipediji")
        }
    }
}

@Composable
fun DetailChip(text: String, score: Int) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val chipColor = getColorForScore(score)

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .background(chipColor) // Apply background color first
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .background(color = chipColor, shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$score/5",
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


fun getColorForScore(score: Int): Color {
    return when (score) {
        in 0..1 -> Color.Red
        in 2..3 -> Color.Yellow
        in 4..5 -> Color.Green
        else -> Color.Gray
    }
}