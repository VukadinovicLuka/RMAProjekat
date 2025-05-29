package com.example.prviprojekatrma.breedDetails.api

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiModel(
    val id: String,
    val url: String
)