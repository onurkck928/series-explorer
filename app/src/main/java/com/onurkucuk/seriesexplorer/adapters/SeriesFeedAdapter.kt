package com.onurkucuk.seriesexplorer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.models.Series


class SeriesFeedAdapter(private val seriesList: List<Series>) : RecyclerView.Adapter<SeriesFeedAdapter.SeriesFeedViewHolder>() {

    inner class SeriesFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameText = itemView.findViewById<TextView>(R.id.seriesNameText)
        val leftImage = itemView.findViewById<ImageView>(R.id.leftImage)
        val rightImage = itemView.findViewById<ImageView>(R.id.rightImage)

        fun onBind() {
            nameText.text = "Frieren"
            leftImage.setImageResource(R.drawable.ic_launcher_background)
            rightImage.setImageResource(R.drawable.ic_launcher_background)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesFeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.series_feed_recycler_row, parent, false)
        return SeriesFeedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    override fun onBindViewHolder(holder: SeriesFeedViewHolder, position: Int) {
        holder.onBind()
    }
}