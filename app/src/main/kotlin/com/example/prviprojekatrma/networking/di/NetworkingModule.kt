package com.example.prviprojekatrma.networking.di

import com.example.prviprojekatrma.networking.serialization.AppJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CatApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LeaderboardApi

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val updatedRequest = chain.request().newBuilder()
                    .addHeader("CustomHeader", "CustomValue")
                    .addHeader("x-api-key", "live_s3l2b7ejOGqUU8v4DUvUsqm2hbsAOMhHQs1S4E6WTX5DiOtUYsRZVQTbeMRSZjTP")
                    .build()
                chain.proceed(updatedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @CatApi
    @Singleton
    @Provides
    fun provideCatApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @LeaderboardApi
    @Singleton
    @Provides
    fun provideLeaderboardApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
