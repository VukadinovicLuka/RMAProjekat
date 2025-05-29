package com.example.prviprojekatrma.breeds.api

import com.example.prviprojekatrma.breedDetails.api.ImageApiModel
import com.example.prviprojekatrma.breeds.api.model.BreedApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("breeds/{breeds_id}")
    suspend fun getBreedDetails(@Path("breeds_id") breedId: String): BreedApiModel

    @GET("images/{image_id}")
    suspend fun getBreedImage(@Path("image_id") imageId: String): ImageApiModel

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 0, // Podrazumevani broj slika
        @Query("order") order: String = "ASC"  // Redosled slika
    ): List<ImageApiModel>

}