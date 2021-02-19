package com.example.gamecompanion.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamecompanion.services.UserManagerService

class TwitchViewModel():ViewModel() {

    lateinit var userManager: UserManagerService

    val isLoggenIn = MutableLiveData<Boolean>()

    val topGames = MutableLiveData<List<Any>>()

    private fun checkUserAvailability(){
        val isLoggedIn = userManager.getAccessToken() != null
        this.isLoggenIn.postValue(isLoggedIn)
    }

    private fun logOut(){
        this.isLoggenIn.postValue(false)
    }

    init {
        topGames.postValue(listOf())
    }

}