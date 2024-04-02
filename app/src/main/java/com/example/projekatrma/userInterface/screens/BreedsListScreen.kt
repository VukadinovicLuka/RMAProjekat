package com.example.projekatrma.userInterface.screens

// BreedsListScreen.kt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.projekatprma.model.Breed
import com.example.projekatprma.viewModel.BreedsListViewModel

@Composable
fun BreedsListScreen(viewModel: BreedsListViewModel) {
    val breedsList = viewModel.breedsList.value

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(breedsList) { breed ->
            BreedListItem(breed)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun BreedListItem(breed: Breed) {
    // Kartica ili red za svaku rasu
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = breed.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = breed.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
