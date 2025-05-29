package com.example.prviprojekatrma.gallery.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "breed_images",indices = [Index(value = ["imageUrl"], unique = true)])
data class ImageData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val breedId: String,
    val imageUrl: String
)