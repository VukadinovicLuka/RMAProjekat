package com.example.prviprojekatrma.breeds.api.di

import com.example.prviprojekatrma.breeds.api.BreedsApi
import com.example.prviprojekatrma.breeds.db.BreedsDao
import com.example.prviprojekatrma.db.AppDatabase
import com.example.prviprojekatrma.networking.di.NetworkingModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedModule {

    @Provides
    @Singleton
    fun provideBreedsApi(@NetworkingModule.CatApi retrofit: Retrofit): BreedsApi = retrofit.create(BreedsApi::class.java)

    @Provides
    fun provideBreedsDao(database: AppDatabase): BreedsDao {
        return database.BreedsDao()
    }
}
