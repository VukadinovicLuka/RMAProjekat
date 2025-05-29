package com.example.prviprojekatrma.leaderboard.api.di

import com.example.prviprojekatrma.leaderboard.api.LeaderboardApi
import com.example.prviprojekatrma.networking.di.NetworkingModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideLeaderboardApi(@NetworkingModule.LeaderboardApi retrofit: Retrofit): LeaderboardApi = retrofit.create(LeaderboardApi::class.java)
}
