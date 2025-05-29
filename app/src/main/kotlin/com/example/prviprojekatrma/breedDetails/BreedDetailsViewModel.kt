package com.example.prviprojekatrma.breedDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prviprojekatrma.breeds.repository.BreedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.prviprojekatrma.breeds.mappers.asBreedDetailUiModel
import com.example.prviprojekatrma.navigation.breeds_id
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    //@BreedId val breedId: String,
    private val repository: BreedsRepository
) : ViewModel() {

    private val breedId: String = savedStateHandle.breeds_id

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) = _state.update(reducer)

    init {
        fetchBreedDetails()
    }

    private fun fetchBreedDetails() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val breed = withContext(Dispatchers.IO) {
                    repository.getBreedById(breedId).asBreedDetailUiModel()
                }

                setState { copy(breedDetail = breed) }
                setState { copy(breedImageURL = breed.imageUrl) }

            } catch (error: Exception) {
                // Handle error
                setState { copy(error = BreedDetailsState.DetailsError.DataUpdateFailed(cause = error)) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}
