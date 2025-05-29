package com.example.prviprojekatrma.breeds.db

import com.example.prviprojekatrma.breeds.list.model.BreedUiModel

data class BreedProfile (
    val id: String,
    val name: String,
    val description: String,
    val origin: String?,
    val temperament: String,
    val lifeSpan: String,
    val weight: String,
    val adaptability: Int?,
    val energyLevel: Int?,
    val affectionLevel: Int?,
    val childFriendly: Int?,
    val dogFriendly: Int?,
    val imageUrl: String,
    val altNames: String,
)

