package com.example.prviprojekatrma.breedDetails.model

import com.example.prviprojekatrma.breeds.api.model.Weight

data class BreedDetailUiModel(
    val id: String,
    val temperament: String,
    val name: String,
    val alt_names: String,
    val description: String,
    val life_span: String,
    val origin: String,
    val weight: Weight,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val is_rare: Boolean,
    val wikipedia_url: String,
    val imageUrl: String,
    val reference_image_id: String?,
)