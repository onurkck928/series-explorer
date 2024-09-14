package com.onurkucuk.seriesexplorer.models

data class SeriesResponse(
    val page: Int,
    val results: MutableList<Series>,
    val total_pages: Int,
    val total_results: Int
)