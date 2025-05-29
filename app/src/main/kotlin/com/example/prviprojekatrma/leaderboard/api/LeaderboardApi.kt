package com.example.prviprojekatrma.leaderboard.api

import retrofit2.http.Query
import com.example.prviprojekatrma.leaderboard.post.PostResponse
import com.example.prviprojekatrma.leaderboard.post.PostResult
import com.example.prviprojekatrma.quiz.QuizResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LeaderboardApi {

    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("category") category: Int): List<QuizResult>

    @POST("leaderboard")
    suspend fun postResult(@Body result: PostResult): PostResponse
}