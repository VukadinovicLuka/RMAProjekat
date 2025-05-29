package com.example.prviprojekatrma.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breed_data")
data class BreedData(
    @PrimaryKey val id: String,
    val name: String,
    val temperament: String,
    val description: String,
    val altNames: String,
    val origin: String,
    val lifeSpan: String,
    val weightImperial: String,
    val weightMetric: String,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val grooming: Int,
    val healthIssues: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val socialNeeds: Int,
    val strangerFriendly: Int,
    val vocalisation: Int,
    val wikipediaUrl: String,
    val isRare: Boolean?,
    val imageUrl: String,
    val referenceImageId: String?
)