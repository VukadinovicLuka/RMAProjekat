package com.example.prviprojekatrma.drawer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppDrawerViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(AppDrawerContract.UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: AppDrawerContract.UiState.() -> AppDrawerContract.UiState) = _state.update(reducer)

}