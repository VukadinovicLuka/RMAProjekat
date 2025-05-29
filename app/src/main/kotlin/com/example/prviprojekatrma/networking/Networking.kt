package com.example.prviprojekatrma.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.example.prviprojekatrma.networking.serialization.AppJson
import java.util.concurrent.TimeUnit

/*
 * Order of okhttp interceptors is important. If logging was first,
 * it would not log the custom header.
 */
val okHttpClient = OkHttpClient.Builder()
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
    // Set the connect timeout
    .connectTimeout(60, TimeUnit.SECONDS) // Increase to 20 seconds
    // Set the read timeout
    .readTimeout(60, TimeUnit.SECONDS) // Increase to 20 seconds
    // Set the write timeout
    .writeTimeout(60, TimeUnit.SECONDS) // Increase to 20 seconds
    .build()



val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()
