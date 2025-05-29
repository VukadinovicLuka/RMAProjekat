package com.example.prviprojekatrma.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(breed: BreedData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedData>)

    @Query("SELECT * FROM breed_data")
    fun getAllBreeds(): Flow<List<BreedData>>

    @Query("SELECT * FROM breed_data WHERE id = :breedId")
    suspend fun getBreedById(breedId: String): BreedData?

    @Query("SELECT name FROM breed_data WHERE name != :exclude ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomBreedNames(exclude: String, count: Int): List<String>

    @Query("SELECT DISTINCT temperament FROM breed_data WHERE temperament NOT IN (:exclude) ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTemperament(exclude: List<String>): String

    @Query("SELECT DISTINCT temperament FROM breed_data WHERE temperament NOT IN (:exclude) ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomTemperaments(exclude: List<String>, count: Int): List<String>



}

