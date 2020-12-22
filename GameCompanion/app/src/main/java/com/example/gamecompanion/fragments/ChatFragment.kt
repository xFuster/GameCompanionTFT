package com.example.gamecompanion.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R
import com.example.gamecompanion.adapter.ChatAdapter
import com.example.gamecompanion.models.Chat
import com.example.gamecompanion.models.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatFragment: Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var edTxtMessage: EditText
    private lateinit var btnSendMessage: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var chatAdapter:ChatAdapter

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
        getChats()
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
        chatAdapter = ChatAdapter(chatList = listOf())
        recyclerView.adapter = chatAdapter
    }

    private fun initListeners(){
        btnSendMessage.setOnClickListener{
            val message = edTxtMessage.text.toString()
            // validate
            if(message.isBlank())return@setOnClickListener
            // Get userId
            Log.i("Chat", "Pronto")
            Firebase.auth.currentUser?.uid?.let { userId: String ->
                //1 - Get user object
                firestore
                    .collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = it.result?.toObject(UserModel::class.java)?.let { userModel: UserModel ->
                                val chat = Chat(
                                    userId = Firebase.auth.currentUser?.uid,
                                    message = message,
                                    sendAt = Date().time,
                                    isSent = false,
                                    imageUrl = null,
                                    userName = userModel.userName,
                                    avatarUrl = null
                                )
                                firestore
                                    .collection("chat")
                                    .add(chat)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Log.i("Chat", "Success uploading message")
                                            getChats()
                                        } else {
                                            Log.w("Chat", "Error uploading message")
                                        }
                                    }

                            } ?: run {

                            }
                        } else {

                        }
                    }

            } ?: kotlin.run {

            }

        }
    }

    private fun getChats(){
        firestore
            .collection("chat")
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                  val chats:List<Chat> = it.result?.documents?.mapNotNull{ it.toObject(Chat::class.java) }.orEmpty()
                    chatAdapter.chatList = chats
                    chatAdapter.notifyDataSetChanged()
                }else{
                    Log.w("Chat", "Error al cargar el chat")
                }
            }
    }
}