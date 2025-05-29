package com.example.prviprojekatrma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prviprojekatrma.breeds.db.BreedData
import com.example.prviprojekatrma.breeds.db.BreedsDao
import com.example.prviprojekatrma.gallery.db.ImageData
import com.example.prviprojekatrma.gallery.di.GalleryDao
import com.example.prviprojekatrma.score.db.ScoreDao
import com.example.prviprojekatrma.score.db.ScoreData


@Database(
    entities = [
        BreedData::class,
        ImageData::class,
        ScoreData::class,
    ],
    version = 6,
    exportSchema = true,
)

abstract class AppDatabase : RoomDatabase (){
    abstract fun BreedsDao() : BreedsDao
    abstract fun ScoreDao() :ScoreDao
    abstract fun GalleryDao() : GalleryDao
}
