package com.example.gamecompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R
import com.example.gamecompanion.models.News
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter (var newsList: List<News>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // Inflate view (xml layout) -> viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(newsView)
    }

    // Total items(para scrollear)
    override fun getItemCount(): Int {
        return newsList.count()
    }

    // Update
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]

        holder.bodyTextView.text = news.body
        holder.titleTextView.text = news.title

        val stamp = news.datePublished.toString().toLong()
        var sdf = SimpleDateFormat("HH:mm")
        var netDate = Date(stamp)
        var date = sdf.format(netDate)

        holder.hourTextView.text = date.toString()
        sdf = SimpleDateFormat("dd/MM")
        netDate = Date(stamp)
        date = sdf.format(netDate)
        holder.dayTextView.text = date.toString()


    }

    // Maps view xml -> Kotlin
    inner class NewsViewHolder(view: View): RecyclerView.ViewHolder(view){
        // Init views
        val bodyTextView: TextView = view.findViewById(R.id.messageNewId)
        val titleTextView: TextView = view.findViewById(R.id.titleNewId)
        val dayTextView: TextView = view.findViewById(R.id.newSentAtDay)
        val hourTextView: TextView = view.findViewById(R.id.newSentAtHour)

        val cardView: CardView = view.findViewById(R.id.cardViewId)
    }

}