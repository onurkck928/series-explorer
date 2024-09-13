package com.onurkucuk.seriesexplorer.network

import com.onurkucuk.seriesexplorer.models.SeriesResponse
import com.onurkucuk.seriesexplorer.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesAPI {

    @GET("tv/top_rated")
    suspend fun getSeriesList(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<SeriesResponse>

    @GET("search/tv")
    suspend fun searchForSeries(
        @Query("query")
        searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<SeriesResponse>

}


