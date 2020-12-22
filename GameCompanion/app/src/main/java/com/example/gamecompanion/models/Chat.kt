package com.example.gamecompanion.models

data class Chat(
    val userId:String? = null,
    val message:String? = null,
    val sendAt:Long? = null,
    val isSent:Boolean? = null,
    val imageUrl:String? = null,
    val userName:String? = null,
    val avatarUrl:String? = null
)