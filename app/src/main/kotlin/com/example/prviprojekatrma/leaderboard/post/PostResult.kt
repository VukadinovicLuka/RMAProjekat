package com.example.prviprojekatrma.leaderboard.post

import kotlinx.serialization.Serializable

@Serializable
data class PostResult(
    val nickname: String,
    val result: Double,
    val category: Int
)