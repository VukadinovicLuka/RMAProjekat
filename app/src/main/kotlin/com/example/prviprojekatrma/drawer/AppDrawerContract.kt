package com.example.prviprojekatrma.drawer

interface AppDrawerContract {

    data class UiState(
        val adminName: String = "",
    )

    sealed class UiEvent {
        data object LogoutConfirmed : UiEvent()
    }

}