package com.example.prviprojekatrma.leaderboard

import com.example.prviprojekatrma.quiz.QuizResult

interface LeaderboardContract {

    data class LeaderboardState(
        val results: List<QuizResult> = emptyList(),
        val userOccurrences: Map<String, Int> = emptyMap(),
        val error: LeaderboardError? = null,
        val loading: Boolean = false
    )

    sealed class LeaderboardUiEvent {
        data object LoadLeaderboard : LeaderboardUiEvent()
    }

    sealed class LeaderboardError {
        data class LoadLeaderboardFailed(val cause: Throwable? = null) : LeaderboardError()
    }
}
