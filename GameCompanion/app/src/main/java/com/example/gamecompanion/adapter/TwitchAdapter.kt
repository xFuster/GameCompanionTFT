package com.example.gamecompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R
import com.example.gamecompanion.models.Twitch

class TwitchAdapter(var twitchList:List<Twitch>): RecyclerView.Adapter<TwitchAdapter.TwitchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwitchAdapter.TwitchViewHolder {
        val twitchView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_twitch, parent, false)
        return TwitchViewHolder(twitchView)
    }

    override fun getItemCount(): Int {
        return twitchList.count()
    }

    override fun onBindViewHolder(holder: TwitchAdapter.TwitchViewHolder, position: Int) {
        holder.txtStreamer.text = twitchList[position].boxArtUrl
        holder.txtTittle.text = twitchList[position].name
        holder.txtViewers.text = twitchList[position].nameID.toString()
    }

    inner class TwitchViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtStreamer: TextView = view.findViewById(R.id.txtStreamer)
        val txtTittle: TextView = view.findViewById(R.id.txtTittle)
        val txtViewers: TextView = view.findViewById(R.id.txtViewers)
    }

}