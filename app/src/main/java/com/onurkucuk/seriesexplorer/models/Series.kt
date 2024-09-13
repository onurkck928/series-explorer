package com.onurkucuk.seriesexplorer.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Series(

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int? = null,
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val id: Int,
    val name: String,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int
)