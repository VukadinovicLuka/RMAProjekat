package com.example.prviprojekatrma.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prviprojekatrma.auth.AuthData
import com.example.prviprojekatrma.auth.AuthStore
import com.example.prviprojekatrma.leaderboard.repository.LeaderboardRepository
import com.example.prviprojekatrma.score.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authStore: AuthStore,
    private val scoreRepository: ScoreRepository,
    private val leaderboardRepository: LeaderboardRepository // Dodato za pristup leaderboard podacima
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.ProfileState())
    val state = _state.asStateFlow()

    private val events = MutableSharedFlow<ProfileContract.ProfileUiEvent>()
    fun setEvent(event: ProfileContract.ProfileUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        loadProfileData()
        loadAllScores()
        loadGlobalRank() // Učitavanje globalnog ranga
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is ProfileContract.ProfileUiEvent.UpdateProfile -> updateProfile(it.name, it.lastname, it.email, it.nickname)
                    ProfileContract.ProfileUiEvent.LoadProfileData -> loadProfileData()
                    ProfileContract.ProfileUiEvent.LoadAllScores -> loadAllScores()
                    ProfileContract.ProfileUiEvent.LoadGlobalRank -> loadGlobalRank() // Obrada događaja za učitavanje globalnog ranga
                }
            }
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val bestScore = scoreRepository.getBestScore()

                setState {
                    copy(
                        name = authStore.authData.value.name,
                        nickname = authStore.authData.value.nickname,
                        lastname = authStore.authData.value.lastname,
                        email = authStore.authData.value.email,
                        bestScore = bestScore ?: 0,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                setState {
                    copy(
                        error = ProfileContract.ProfileError.LoadProfileDataFailed(cause = e),
                        loading = false
                    )
                }
            }
        }
    }

    private fun loadAllScores() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val scores = scoreRepository.getAllScores()
                setState {
                    copy(
                        scores = scores.sortedByDescending { it.attemptedAt },
                        loading = false
                    )
                }
            } catch (e: Exception) {
                setState {
                    copy(
                        error = ProfileContract.ProfileError.LoadAllScoresFailed(cause = e),
                        loading = false
                    )
                }
            }
        }
    }

    private fun loadGlobalRank() {
        viewModelScope.launch {
            try {
                val leaderboard = leaderboardRepository.getLeaderboard(1)
                Log.d("ProfileViewModel","${authStore.authData.value.nickname.trim().replace("\\s".toRegex(), "_")}")
                val userRank = leaderboard.indexOfFirst { it.nickname == authStore.authData.value.nickname.trim().replace("\\s".toRegex(), "_") } + 1
                setState {
                    copy(globalRank = if (userRank > 0) userRank else -1)
                }
            } catch (e: Exception) {
                setState {
                    copy(error = ProfileContract.ProfileError.LoadGlobalRankFailed(cause = e))
                }
            }
        }
    }

    private fun updateProfile(name: String, lastname: String, email: String, nickname: String) {
        viewModelScope.launch {
            // Validate nickname
            if (!isValidNickname(nickname)) {
                setState {
                    copy(error = ProfileContract.ProfileError.InvalidNickname)
                }
                return@launch
            }

            try {
                val newAuthData = AuthData(
                    name = name,
                    lastname = lastname,
                    email = email,
                    nickname = nickname
                )
                authStore.updateAuthData(newAuthData)
                loadProfileData() // Reload the profile to reflect changes
            } catch (e: Exception) {
                setState {
                    copy(error = ProfileContract.ProfileError.ProfileUpdateFailed(cause = e))
                }
            }
        }
    }

    // Function to check if the nickname is valid
    private fun isValidNickname(nickname: String): Boolean {
        val nicknamePattern = "^[a-zA-Z0-9_]+$"
        return nickname.matches(Regex(nicknamePattern))
    }


    private fun setState(reducer: ProfileContract.ProfileState.() -> ProfileContract.ProfileState) =
        _state.update(reducer)
}
