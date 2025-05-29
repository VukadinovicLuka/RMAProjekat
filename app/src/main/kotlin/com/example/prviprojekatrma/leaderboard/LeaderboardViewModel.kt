package com.example.prviprojekatrma.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prviprojekatrma.leaderboard.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardContract.LeaderboardState())
    val state = _state.asStateFlow()

    private val events = MutableSharedFlow<LeaderboardContract.LeaderboardUiEvent>()
    fun setEvent(event: LeaderboardContract.LeaderboardUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        loadLeaderboard()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    LeaderboardContract.LeaderboardUiEvent.LoadLeaderboard -> loadLeaderboard()
                }
            }
        }
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            try {
                val results = repository.getLeaderboard(1)
                val userOccurrences = repository.getUserOccurrences(1)
                _state.value = _state.value.copy(results = results, userOccurrences = userOccurrences, loading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = LeaderboardContract.LeaderboardError.LoadLeaderboardFailed(cause = e), loading = false)
            }
        }
    }
}
