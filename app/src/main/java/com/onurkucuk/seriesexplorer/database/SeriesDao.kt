package com.onurkucuk.seriesexplorer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.onurkucuk.seriesexplorer.models.Series

@Dao
interface SeriesDao {

    @Insert
    suspend fun saveSeries(vararg series: Series)

    @Query("DELETE FROM series WHERE id LIKE :seriesID")
    suspend fun removeSeries(seriesID: Int)

    @Query("SELECT * FROM series")
    fun getSavedSeries(): LiveData<List<Series>>

    @Query("SELECT * FROM series WHERE name LIKE :seriesName")
    fun findFromSavedSeries(seriesName: String): LiveData<List<Series>>
}