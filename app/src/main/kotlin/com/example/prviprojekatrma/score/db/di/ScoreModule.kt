package com.example.prviprojekatrma.score.db.di

import com.example.prviprojekatrma.db.AppDatabase
import com.example.prviprojekatrma.score.db.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScoreModule {

    @Provides
    fun provideScoreDao(database: AppDatabase): ScoreDao {
        return database.ScoreDao()
    }

}