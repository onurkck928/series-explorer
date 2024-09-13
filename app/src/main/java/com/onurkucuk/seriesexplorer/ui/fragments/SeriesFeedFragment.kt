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
import com.onurkucuk.seriesexplorer.databinding.FragmentSeriesFeedBinding
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.network.SeriesRetrofitInstance
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel

import kotlinx.coroutines.runBlocking


class SeriesFeedFragment : Fragment() {

    //View Binding
    private var _binding: FragmentSeriesFeedBinding? = null
    private val binding get() = _binding!!

    // Properties
    lateinit var viewModel: SeriesViewModel
    lateinit var seriesFeedAdapter: SeriesFeedAdapter
    val seriesList = mutableListOf<Series>()
    lateinit var seriesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSeriesFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()




    }

    private fun setupRecyclerView() {

        seriesFeedAdapter = SeriesFeedAdapter(seriesList)
        seriesRecyclerView = binding.seriesFeedRecycler

        seriesRecyclerView.adapter = seriesFeedAdapter
        seriesRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}