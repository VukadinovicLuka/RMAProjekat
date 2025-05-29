package com.example.prviprojekatrma.score.repository

import com.example.prviprojekatrma.db.AppDatabase
import com.example.prviprojekatrma.score.db.ScoreData
import javax.inject.Inject

class ScoreRepository @Inject constructor(
    private val database: AppDatabase

){

    suspend fun storeScore(scoreData: ScoreData){
        database.ScoreDao().insert(scoreData)
    }

    suspend fun getBestScore(): Int? {
        return database.ScoreDao().getBestScore()
    }

    suspend fun getAllScores(): List<ScoreData> {
        return database.ScoreDao().getAllScores()
    }

}