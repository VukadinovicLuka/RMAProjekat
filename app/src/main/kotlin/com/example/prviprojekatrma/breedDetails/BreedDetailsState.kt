package com.example.prviprojekatrma.breedDetails

import com.example.prviprojekatrma.breedDetails.model.BreedDetailUiModel

data class BreedDetailsState(
    val breedId: String,
    val breedDetail: BreedDetailUiModel? = null,
    val breedImageURL: String? = null,
    val loading: Boolean = false,
    val error: DetailsError? = null
) {
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable) : DetailsError()
    }
}