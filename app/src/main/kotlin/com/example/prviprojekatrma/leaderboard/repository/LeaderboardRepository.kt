package com.example.prviprojekatrma.leaderboard.repository

import android.util.Log
import com.example.prviprojekatrma.leaderboard.api.LeaderboardApi
import com.example.prviprojekatrma.leaderboard.post.PostResult
import com.example.prviprojekatrma.quiz.QuizResult
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(private val api: LeaderboardApi) {

    suspend fun getLeaderboard(category: Int): List<QuizResult> {
        return api.getLeaderboard(category)
    }

    suspend fun getUserOccurrences(category: Int): Map<String, Int> {
        val leaderboard = getLeaderboard(category)
        return leaderboard.groupingBy { it.nickname }.eachCount()
    }

    suspend fun postResult(nickname: String, result: Double, category: Int) {
        val postResult = PostResult(nickname, result, category)
        Log.d("LeaderboardRepository", "Posting result: $postResult")
        api.postResult(PostResult(nickname, result, category))
    }
}