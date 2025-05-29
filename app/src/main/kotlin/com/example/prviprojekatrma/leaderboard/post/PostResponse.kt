package com.example.prviprojekatrma.leaderboard.post

import com.example.prviprojekatrma.quiz.QuizResult
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val result: QuizResult,
    val ranking: Int
)