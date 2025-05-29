package com.example.prviprojekatrma.auth

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStore @Inject constructor(
    private val dataStore: DataStore<AuthData>
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val authData = dataStore.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { dataStore.data.first() }, // blokira sve sto se radi
        )

    suspend fun updateAuthData(newAuthData: AuthData) {
        dataStore.updateData {
            newAuthData
        }
    }

     suspend fun getAuthData(): String? {
        return dataStore.data.first().name
    }

    suspend fun isAuthenticated(): Boolean {
        return getAuthData() != ""
    }
}
