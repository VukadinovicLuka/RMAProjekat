package com.example.projekatprma.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekatprma.model.Breed
import com.example.projekatprma.repository.BreedsRepository
import kotlinx.coroutines.launch

class BreedsListViewModel : ViewModel() {

    private val breedsRepository = BreedsRepository()
    // Inicijalizacija prazne liste rasa
    var breedsList = mutableStateOf<List<Breed>>(listOf())
        private set

    init {
        viewModelScope.launch {
            breedsList.value = breedsRepository.getBreeds()
        }
    }
}
