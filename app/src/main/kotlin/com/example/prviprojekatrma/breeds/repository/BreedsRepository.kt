package com.example.prviprojekatrma.breeds.repository

import android.util.Log
import com.example.prviprojekatrma.breedDetails.api.ImageApiModel
import com.example.prviprojekatrma.breeds.api.BreedsApi
import com.example.prviprojekatrma.breeds.db.BreedData
import com.example.prviprojekatrma.breeds.mappers.asBreedDbModel
import com.example.prviprojekatrma.db.AppDatabase
import com.example.prviprojekatrma.gallery.db.ImageData
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class BreedsRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase
) {
    suspend fun fetchAllBreeds() {
        val breeds = breedsApi.getAllBreeds()

        val breedsWithImages = breeds.map { breed ->
            val imageUrl = getBreedImage(breed.reference_image_id)?.url
            breed.copy(url = imageUrl ?: "")
        }

        database.BreedsDao().insertAll(breedsWithImages.map { it.asBreedDbModel() })
    }

    suspend fun observeBreeds() = database.BreedsDao().getAllBreeds()

    suspend fun getBreedById(breedId: String): BreedData {

        val breed = database.BreedsDao().getBreedById(breedId)
        if (breed != null) {
            Log.d("BreedsRepository","Uzeo podatke iz baze")
            return breed
        }

        val breedFromApi = breedsApi.getBreedDetails(breedId)
        val breedData = breedFromApi.asBreedDbModel()
        database.BreedsDao().insert(breedData)
        return breedData
    }

    suspend fun getRandomBreedImage(): ImageData {
        return database.GalleryDao().getRandomBreedImage()
    }

    suspend fun getRandomBreedNames(exclude: String, count: Int): List<String> {
        return database.BreedsDao().getRandomBreedNames(exclude, count)
    }

    suspend fun getRandomTemperament(exclude: List<String>): String {
        // Pretraži sve temperamente, izaberi jedan nasumičan koji nije u listi exclude
        val allTemperaments = database.BreedsDao().getAllBreeds().first()
            .map { it.temperament.split(", ") }
            .flatten()
            .distinct()

        return allTemperaments.filter { it !in exclude }.random()
    }

    suspend fun getRandomTemperaments(exclude: List<String>, count: Int): List<String> {
        // Pretraži sve temperamente, izaberi više nasumičnih koji nisu u listi exclude
        val allTemperaments = database.BreedsDao().getAllBreeds().first()
            .map { it.temperament.split(", ") }
            .flatten()
            .distinct()

        return allTemperaments.filter { it !in exclude }.shuffled().take(count)
    }

    suspend fun fetchAndStoreBreedImages(breedId: String) {
        val images = breedsApi.getBreedImages(breedId, page = 0, limit = 10)
        val breedImages = images.map { ImageData(breedId = breedId, imageUrl = it.url) }
        database.GalleryDao().insertAllImages(breedImages)
    }

    suspend fun getBreedImage(imageId: String?): ImageApiModel? {
        return imageId?.let { breedsApi.getBreedImage(it) }
    }

    fun getImagesByBreedId(breedId: String) = database.GalleryDao().getImagesByBreedId(breedId)
}

