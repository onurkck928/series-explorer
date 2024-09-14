package com.onurkucuk.seriesexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onurkucuk.seriesexplorer.databinding.FragmentSeriesDetailsBinding
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel
import com.onurkucuk.seriesexplorer.util.Constants.Companion.BASE_IMAGE_URL
import com.squareup.picasso.Picasso


class SeriesDetailsFragment : Fragment() {

    lateinit var viewModel: SeriesViewModel
    //View Binding
    private var _binding: FragmentSeriesDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var series: Series

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSeriesDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        series = SeriesViewModel.seriesList[arguments?.getInt("position") ?: 0]
        binding.textView.text = series.name
        Picasso.get().load("$BASE_IMAGE_URL${series.backdrop_path}").into(binding.imageView)
    }



}