package com.onurkucuk.seriesexplorer.network

import com.onurkucuk.seriesexplorer.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SeriesRetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val seriesAPI by lazy {
            retrofit.create(SeriesAPI::class.java)
        }


    }
}