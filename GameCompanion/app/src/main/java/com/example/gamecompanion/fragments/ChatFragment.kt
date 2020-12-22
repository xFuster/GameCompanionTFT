package com.example.gamecompanion.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R
import com.example.gamecompanion.adapter.ChatAdapter
import com.example.gamecompanion.models.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment: Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var edTxtMessage: EditText
    private lateinit var btnSendMessage: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init firestore
        firestore = Firebase.firestore
        // Init views
        initViews(view)
        // Init recycler view
        initReciclerView()
        initListeners()
    }

    private fun initViews(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        edTxtMessage = view.findViewById(R.id.edTxtMessage)
        btnSendMessage = view.findViewById(R.id.btnSendMessage)
    }

    private fun initReciclerView(){
        // Layout Manager
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        // Adapter
        val adapter = ChatAdapter(chatList = listOf("Chat 0","Chat 1", "Chat 2", "Chat 3", "Chat 4", "Chat 5", "Chat 6", "Chat 7", "Chat 8", "Chat 9"))
        recyclerView.adapter = adapter
    }

    private fun initListeners(){
        btnSendMessage.setOnClickListener{
            val message = edTxtMessage.text.toString()
            // validate
            if(message.isBlank())return@setOnClickListener
            val chat = Chat(message = message)
            firestore
                .collection("chat")
                .add(chat)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Log.i("Chat", "Succes uploading message")
                    }else{
                        Log.w("Chat", "Error no se ha podido mandar el mensaje")
                    }
                }

        }
    }
}