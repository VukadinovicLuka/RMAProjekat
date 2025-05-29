package com.example.prviprojekatrma.gallery

interface GalleryContract {

    data class GalleryState(
        val images: List<String> = emptyList(),
        val loading: Boolean = false,
        val error: GalleryError? = null
    )

    sealed class GalleryUiEvent {
        data object LoadNextPage : GalleryUiEvent()
        data object LoadPreviousPage : GalleryUiEvent()
        data class ImageClicked(val index: Int) : GalleryUiEvent()
    }

    sealed class GalleryError {
        data class FetchImagesFailed(val cause: Throwable? = null) : GalleryError()
    }
}
