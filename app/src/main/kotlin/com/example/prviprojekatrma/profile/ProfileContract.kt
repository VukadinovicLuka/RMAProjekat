package com.example.prviprojekatrma.profile

import com.example.prviprojekatrma.score.db.ScoreData

interface ProfileContract {

    data class ProfileState(
        val name: String = "",
        val nickname: String = "",
        val lastname: String = "",
        val email: String = "",
        val bestScore: Int = 0,
        val globalRank: Int = -1, // Dodato polje za globalni rang
        val scores: List<ScoreData> = emptyList(),
        val loading: Boolean = false,
        val error: ProfileError? = null
    )

    sealed class ProfileUiEvent {
        data class UpdateProfile(val name: String, val lastname: String, val email: String, val nickname: String) : ProfileUiEvent()
        data object LoadProfileData : ProfileUiEvent()
        data object LoadAllScores : ProfileUiEvent()
        data object LoadGlobalRank : ProfileUiEvent()
    }

    sealed class ProfileError {
        data class ProfileUpdateFailed(val cause: Throwable? = null) : ProfileError()
        data class LoadProfileDataFailed(val cause: Throwable? = null) : ProfileError()
        data class LoadAllScoresFailed(val cause: Throwable? = null) : ProfileError()
        data class LoadGlobalRankFailed(val cause: Throwable? = null) : ProfileError()
        object InvalidNickname : ProfileError()

    }
}
