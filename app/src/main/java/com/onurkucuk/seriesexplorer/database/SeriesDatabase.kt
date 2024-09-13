package com.onurkucuk.seriesexplorer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onurkucuk.seriesexplorer.models.Series

@Database(
    entities = [Series::class],
    version = 1
)
abstract class SeriesDatabase : RoomDatabase() {

    abstract fun getSeriesDao(): SeriesDao

    companion object {
        @Volatile
        private var instance: SeriesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SeriesDatabase::class.java,
                "series-database"
            ).build()
    }

}