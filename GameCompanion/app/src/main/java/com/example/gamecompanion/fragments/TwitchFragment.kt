package com.example.gamecompanion.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.Activity.TwitchLoginActivity
import com.example.gamecompanion.R
import com.example.gamecompanion.adapter.ChatAdapter
import com.example.gamecompanion.adapter.TwitchAdapter
import com.example.gamecompanion.models.Twitch
import com.example.gamecompanion.services.NetworkManager
import com.example.gamecompanion.services.UserManagerService
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TwitchFragment: Fragment(){

    private val TAG = "TwitchLoginActivity"
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnTwitchLogin: Button
    private var OAUTH_CLIENT_ID = "c4rbom4h3nli13i1cvm4iaqo89lp0r"
    private lateinit var twitchAdapter: TwitchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_twitch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        getTopGames()
    }

    override fun onResume() {
        super.onResume()
        checkUserAvailability()
    }

    private fun initViews(view: View){
        btnTwitchLogin = view.findViewById(R.id.btnLoginTwitch)
        recyclerView = view.findViewById(R.id.recViewTwitch)
    }

    private fun initListeners(){
        btnTwitchLogin.setOnClickListener {
            startActivity(Intent(requireActivity(),TwitchLoginActivity::class.java))
        }
    }

    private fun checkUserAvailability(){
        val isLoggedIn = UserManagerService(requireContext()).getAccessToken() != null
        if(isLoggedIn){
            btnTwitchLogin.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }else{
            btnTwitchLogin.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = GridLayoutManager(activity,1)
        // Adapter
        twitchAdapter = TwitchAdapter(twitchList = listOf())
        recyclerView.adapter = twitchAdapter
    }


    private fun getTopGames(){
        val httpClient = NetworkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val accessToken = UserManagerService(requireContext()).getAccessToken()
                // Get top games
                try {
                    val response = httpClient.get<Twitch>("https://api.twitch.tv/helix/games/top?first=12") {
                        header("Client-Id", OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                    }
                    // Change to main thread
                    withContext(Dispatchers.Main){
                        //TODO: Update UI
                        initRecyclerView()
                    }
                }catch (t:Throwable){

                }
            }
        }
    }
}