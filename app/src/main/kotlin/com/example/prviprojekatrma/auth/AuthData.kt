package com.example.prviprojekatrma.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val nickname: String,
    val name: String,
    val lastname: String,
    val email: String
)