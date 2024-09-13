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
    suspend fun addSeries(vararg series: Series)

    @Delete
    suspend fun removeSeries(series: Series)

    @Query("SELECT * FROM series")
    fun getAllSeries(): LiveData<List<Series>>
}