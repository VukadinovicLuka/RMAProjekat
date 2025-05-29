package com.example.prviprojekatrma.breeds.list

import com.example.prviprojekatrma.breeds.list.model.BreedUiModel

interface BreedListContract {

    data class BreedListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val breeds: List<BreedUiModel> = emptyList(),
        val filteredUsers: List<BreedUiModel> = emptyList(),
        val error: ListError?=null
    )

    sealed class BreedListUiEvent {
        data class SearchQueryChanged(val query: String) : BreedListUiEvent()
        data object ClearSearch : BreedListUiEvent()
        data object CloseSearchMode : BreedListUiEvent()
    }

    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }

}