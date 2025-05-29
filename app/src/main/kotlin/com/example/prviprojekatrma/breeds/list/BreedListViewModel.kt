package com.example.prviprojekatrma.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prviprojekatrma.breeds.mappers.asBreedUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.prviprojekatrma.breeds.repository.BreedsRepository
import com.example.prviprojekatrma.breeds.list.BreedListContract.BreedListUiEvent
import com.example.prviprojekatrma.breeds.list.BreedListContract.BreedListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BreedListState()) // state koji je unutar file
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.update(reducer)

    private val events = MutableSharedFlow<BreedListUiEvent>()
    fun setEvent(event: BreedListUiEvent) = viewModelScope.launch { events.emit(event) }

    private var _isFetched = false

    init {
        observeEvents()
        fetchAllBreeds()
        observeBreeds()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is BreedListUiEvent.CloseSearchMode -> {
                        setState { copy(isSearchMode = false) }
                    }
                    is BreedListUiEvent.SearchQueryChanged -> {
                        filterEvent(query = it.query)
                    }
                    BreedListUiEvent.ClearSearch -> setState { copy(query = "", isSearchMode = false) }
                }
            }
        }
    }

    private fun filterEvent(query: String) {
        viewModelScope.launch {
            try {
                val filteredUsers = _state.value.breeds.filter { it.name.startsWith(query, ignoreCase = true) }
                setState { copy(query = query, filteredUsers = filteredUsers, isSearchMode = true) }
            } catch (error: Exception) {
                Log.e("BreedsListViewModel", "Error filtering breeds", error)
            }
        }
    }

    private fun fetchAllBreeds() {
        if (_isFetched) return
        _isFetched = true

        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds()
                }
            } catch (error: Exception) {
                Log.e("BreedsListViewModel", "Error fetching breeds", error)
                setState { copy(error = BreedListContract.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            repository.observeBreeds()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            loading = false,
                            breeds = it.map { it.asBreedUiModel() },
                        )
                    }
                }
        }
    }
}
