package com.example.prviprojekatrma.login

import androidx.lifecycle.ViewModel
import com.example.prviprojekatrma.auth.AuthData
import com.example.prviprojekatrma.auth.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    suspend fun login(nickname: String, name: String, lastname: String, email: String) {
        if (isValidEmail(email)) {
            val authData = AuthData(nickname, name, lastname, email)
            withContext(Dispatchers.IO) {
                authStore.updateAuthData(authData)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    suspend fun isAuthenticated(): Boolean {
        return withContext(Dispatchers.IO) {
            authStore.isAuthenticated()
        }
    }
}

