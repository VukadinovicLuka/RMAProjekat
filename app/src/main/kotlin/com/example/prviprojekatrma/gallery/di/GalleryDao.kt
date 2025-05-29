package com.example.prviprojekatrma.gallery.di

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prviprojekatrma.gallery.db.ImageData
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllImages(images: List<ImageData>)

    @Query("SELECT * FROM breed_images ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomBreedImage(): ImageData

    @Query("SELECT * FROM breed_images WHERE breedId = :breedId")
    fun getImagesByBreedId(breedId: String): Flow<List<ImageData>>

}