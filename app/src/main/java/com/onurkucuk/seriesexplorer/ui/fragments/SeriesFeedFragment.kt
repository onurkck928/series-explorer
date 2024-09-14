package com.onurkucuk.seriesexplorer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.adapters.SeriesFeedAdapter
import com.onurkucuk.seriesexplorer.databinding.FragmentSeriesFeedBinding
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel
import com.onurkucuk.seriesexplorer.util.Constants.Companion.SEARCH_SERIES_TIME_DELAY
import com.onurkucuk.seriesexplorer.util.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        // for getting all series
        viewModel.seriesList.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is Resources.Success -> {
                    hideProgressBar()
                    seriesFeedAdapter.addSeriesList(response.data?.results)
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this.context,"An error occurred! $it", Toast.LENGTH_LONG).show()
                    }

                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }

        })

        // for getting searched series
        var job: Job? = null
        binding.searchBar.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_SERIES_TIME_DELAY)
                if(editable.toString().isNotEmpty()) {
                    viewModel.getSearchedSeries(editable.toString())
                }
            }
        }

        viewModel.searchedSeries.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is Resources.Success -> {
                    hideProgressBar()
                    seriesFeedAdapter.addSeriesList(response.data?.results)
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this.context,"An error occurred! $it", Toast.LENGTH_LONG).show()
                    }

                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }

        })


    }

    private fun setupRecyclerView() {

        seriesFeedAdapter = SeriesFeedAdapter(seriesList)
        seriesRecyclerView = binding.seriesFeedRecycler

        seriesRecyclerView.adapter = seriesFeedAdapter
            seriesRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
    }

    private fun hideProgressBar() {
        // not implemented yet
    }

    private fun showProgressBar() {
        // not implemented yet
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}