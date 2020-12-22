package com.example.gamecompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R
import com.example.gamecompanion.models.Chat

class ChatAdapter(var chatList:List<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatView:View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return  ChatViewHolder(chatView)
    }

    override fun getItemCount(): Int {
        return chatList.count()
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageTextView.text = chatList[position].message
        holder.userNameTextView.text = chatList[position].userName
        holder.dateTextView.text = chatList[position].sendAt.toString()
    }

    inner class ChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        val messageTextView:TextView = view.findViewById(R.id.txtViewMessage)
        val userNameTextView:TextView = view.findViewById(R.id.txtViewUser)
        val dateTextView:TextView = view.findViewById(R.id.txtViewDate)
    }
}