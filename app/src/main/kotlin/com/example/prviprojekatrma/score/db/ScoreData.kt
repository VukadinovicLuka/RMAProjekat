package com.example.prviprojekatrma.score.db

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalTime

@Entity(tableName="score")
data class ScoreData (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val nickname: String,
    val lastname: String,
    val email: String,
    val score: Int,
    val attemptedAt:String,
    val isPublished: Boolean
    )