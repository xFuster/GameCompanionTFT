package com.example.gamecompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R

class ChatAdapter(private var chatList:List<String>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatView:View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return  ChatViewHolder(chatView)
    }

    override fun getItemCount(): Int {
        return chatList.count()
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageTextView.text = chatList[position]
    }

    inner class ChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        val messageTextView:TextView = view.findViewById(R.id.txtViewMessage)
    }


}