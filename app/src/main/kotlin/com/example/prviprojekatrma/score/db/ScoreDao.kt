package com.example.prviprojekatrma.score.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: ScoreData)

    @Query("SELECT MAX(score) FROM score")
    suspend fun getBestScore(): Int?

    @Query("SELECT * FROM score")
    suspend fun getAllScores(): List<ScoreData>

}