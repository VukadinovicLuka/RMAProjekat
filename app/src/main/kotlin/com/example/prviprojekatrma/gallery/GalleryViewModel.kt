package com.example.prviprojekatrma.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prviprojekatrma.breeds.repository.BreedsRepository
import com.example.prviprojekatrma.navigation.breeds_id
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository
) : ViewModel() {

    private val breedId: String = savedStateHandle.breeds_id

    private val _state = MutableStateFlow(GalleryContract.GalleryState())
    val state = _state.asStateFlow()

    private val events = MutableSharedFlow<GalleryContract.GalleryUiEvent>()
    fun setEvent(event: GalleryContract.GalleryUiEvent) = viewModelScope.launch { events.emit(event) }

    var currentPage = 1
    var totalPages = 1
    private val pageSize = 10

    init {
        observeEvents()
        fetchImages()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is GalleryContract.GalleryUiEvent.LoadNextPage -> loadNextPage()
                    is GalleryContract.GalleryUiEvent.LoadPreviousPage -> loadPreviousPage()
                    is GalleryContract.GalleryUiEvent.ImageClicked -> handleImageClicked(it.index)
                }
            }
        }
    }

    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                // Fetch from API and store in DB
                repository.fetchAndStoreBreedImages(breedId)
                // Retrieve from DB
                repository.getImagesByBreedId(breedId).collect { images ->
                    val imageUrls = images.map { it.imageUrl }
                    setState {
                        copy(
                            images = imageUrls,
                            loading = false
                        )
                    }
                }
            } catch (e: Exception) {
                setState {
                    copy(
                        error = GalleryContract.GalleryError.FetchImagesFailed(cause = e),
                        loading = false
                    )
                }
            }
        }
    }

    private fun loadNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchImages()
        }
    }

    private fun loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchImages()
        }
    }

    private fun handleImageClicked(index: Int) {
        // Handle image click event, e.g., navigate to a detailed view
        // Implement navigation or other logic here
    }

    private fun setState(reducer: GalleryContract.GalleryState.() -> GalleryContract.GalleryState) =
        _state.update(reducer)
}
