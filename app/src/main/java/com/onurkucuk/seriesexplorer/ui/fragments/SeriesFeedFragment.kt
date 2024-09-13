package com.onurkucuk.seriesexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.adapters.SeriesFeedAdapter
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.network.SeriesRetrofitInstance
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel

import kotlinx.coroutines.runBlocking


class SeriesFeedFragment : Fragment() {


    lateinit var viewModel: SeriesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val seriesList = mutableListOf<Series>()

        runBlocking{
            SeriesRetrofitInstance.seriesAPI.getSeriesList().body()?.let {
                for(result in it.results) {
                    println(result.name)
                    seriesList.add(result)
                }
            }
        }

        for(result in seriesList) {
            println(result.name)
        }

        val adapter = SeriesFeedAdapter(seriesList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.seriesFeedRecycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)


    }
}