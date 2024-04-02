package com.example.projekatprma
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekatprma.viewModel.BreedsListViewModel
import com.example.projekatrma.ui.theme.ProjekatRMATheme
import com.example.projekatrma.userInterface.screens.BreedsListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjekatRMATheme {
                // U ovom primeru koristimo viewModel() funkciju za dobavljanje ViewModel instance
                // Ova funkcija koristi ViewModelProvider da bi dobavila ili kreirala ViewModel za ovaj Activity
                val breedsListViewModel: BreedsListViewModel = viewModel()

                // Koristimo Surface composable za primenu pozadine teme aplikacije
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Ovde prikazujemo BreedsListScreen kao deo UI-a MainActivity
                    BreedsListScreen(breedsListViewModel)
                }
            }
        }
    }
}
