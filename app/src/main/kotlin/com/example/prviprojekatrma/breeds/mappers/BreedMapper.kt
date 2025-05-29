package com.example.prviprojekatrma.breeds.mappers

import com.example.prviprojekatrma.breeds.api.model.BreedApiModel
import com.example.prviprojekatrma.breeds.db.BreedData
import com.example.prviprojekatrma.breedDetails.model.BreedDetailUiModel
import com.example.prviprojekatrma.breeds.api.model.Weight
import com.example.prviprojekatrma.breeds.list.model.BreedUiModel

fun BreedApiModel.asBreedDbModel(): BreedData {
    return BreedData(
        id = id,
        name = name,
        temperament = temperament,
        description = description,
        altNames = alt_names,
        origin = origin,
        lifeSpan = life_span,
        weightImperial = weight.imperial,
        weightMetric = weight.metric,
        adaptability = adaptability,
        affectionLevel = affection_level,
        childFriendly = child_friendly,
        dogFriendly = dog_friendly,
        energyLevel = energy_level,
        grooming = grooming,
        healthIssues = health_issues,
        intelligence = intelligence,
        sheddingLevel = shedding_level,
        socialNeeds = social_needs,
        strangerFriendly = stranger_friendly,
        vocalisation = vocalisation,
        wikipediaUrl = wikipedia_url,
        isRare = isRare,
        imageUrl = url,
        referenceImageId = reference_image_id
    )
}

fun BreedData.asBreedDetailUiModel(): BreedDetailUiModel {
    return BreedDetailUiModel(
        id = this.id,
        temperament = this.temperament,
        name = this.name,
        alt_names = this.altNames,
        description = this.description,
        life_span = this.lifeSpan,
        origin = this.origin,
        weight = Weight(imperial = this.weightImperial, metric = this.weightMetric),
        adaptability = this.adaptability,
        affection_level = this.affectionLevel,
        child_friendly = this.childFriendly,
        dog_friendly = this.dogFriendly,
        energy_level = this.energyLevel,
        is_rare = this.isRare ?: false,
        wikipedia_url = this.wikipediaUrl,
        imageUrl = this.imageUrl,
        reference_image_id = this.referenceImageId
    )
}

fun BreedData.asBreedUiModel(): BreedUiModel {
        return BreedUiModel(
            id = this.id,
            name = this.name,
            description = this.description,
            temperament = this.temperament,
            alt_names = this.altNames,
            )
    }
