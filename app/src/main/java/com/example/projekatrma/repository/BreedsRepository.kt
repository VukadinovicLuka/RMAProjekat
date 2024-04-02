package com.example.projekatprma.repository

import com.example.projekatprma.model.Breed
import com.example.projekatprma.network.ApiClient

class BreedsRepository {

    suspend fun getBreeds(): List<Breed> {
        return ApiClient.instance.getBreeds()
    }
}
