package com.example.gamecompanion.services

import android.content.Context

class UserManagerService(context : Context) {

    private val sharedPreferencesUserName = "userInfo"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesUserName,Context.MODE_PRIVATE)

    private val accessTokenKey = "accessToken"

    fun getAccessToken():String?{
        return sharedPreferences.getString(accessTokenKey, null)
    }

    fun saveAccessToken(token: String){
        sharedPreferences.edit().putString(accessTokenKey, token).apply()
    }

}