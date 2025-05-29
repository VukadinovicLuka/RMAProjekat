package com.example.prviprojekatrma.breeds.api.model

import kotlinx.serialization.Serializable

@Serializable
data class BreedApiModel(
    val id: String,
    val name: String,
    val temperament: String,
    val description: String,
    val alt_names: String="",
    val origin: String, // Zemlje porekla
    val life_span: String, // Životni vek
    val weight: Weight, // Prosečna težina
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,
    val wikipedia_url: String="", // URL za Wikipediju
    val isRare: Boolean? = null, // Da li je retka vrsta
    val url: String="", // URL za sliku rase
    val reference_image_id: String? = null,
    //val image: ImageModel?=null,
)

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)

//@Serializable
//data class ImageModel(
//    val id: String,
//    val url: String
//)