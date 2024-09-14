package com.onurkucuk.seriesexplorer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.databinding.SeriesFeedRecyclerRowBinding
import com.onurkucuk.seriesexplorer.models.Series


class SeriesFeedAdapter(private val seriesList: MutableList<Series>) : RecyclerView.Adapter<SeriesFeedAdapter.SeriesFeedViewHolder>() {

    inner class SeriesFeedViewHolder(private val itemBinding: SeriesFeedRecyclerRowBinding) : RecyclerView.ViewHolder(itemBinding.root) {


        fun onBind(series: Series) {
            itemBinding.seriesNameText.text = series.name
            itemBinding.leftImage.setImageResource(R.drawable.ic_launcher_background)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesFeedViewHolder {
        val itemBinding = SeriesFeedRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesFeedViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    override fun onBindViewHolder(holder: SeriesFeedViewHolder, position: Int) {
        holder.onBind(seriesList[position])
    }

    fun addSeriesList(list :List<Series>?) {
        list?.let {
            seriesList.addAll(list)
            notifyDataSetChanged()
        }
    }
}