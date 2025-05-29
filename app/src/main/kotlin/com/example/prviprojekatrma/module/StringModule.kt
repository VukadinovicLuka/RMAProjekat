package com.example.prviprojekatrma.module

import com.example.prviprojekatrma.qualifiers.BreedId
import com.example.prviprojekatrma.qualifiers.GalleryId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StringModule {

    @Provides
    @Singleton
    @BreedId
    fun provideBreedId(): String {
        return "defaultBreedId" // Replace with actual default value or fetch logic
    }

    @Provides
    @Singleton
    @GalleryId
    fun provideGalleryId(): String {
        return "defaultGalleryId" // Replace with actual default value or fetch logic
    }
}