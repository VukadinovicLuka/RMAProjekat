package com.example.prviprojekatrma.gallery.di

import com.example.prviprojekatrma.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {

    @Provides
    fun provideGalleryDao(database: AppDatabase): GalleryDao {
        return database.GalleryDao()
    }

}