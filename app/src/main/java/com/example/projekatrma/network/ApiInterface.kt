package com.example.projekatrma.network

import com.example.projekatprma.model.Breed
import retrofit2.http.GET

interface ApiInterface {
    @GET("v1/breeds")
    suspend fun getBreeds(): List<Breed>
}