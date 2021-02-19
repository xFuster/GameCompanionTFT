package com.example.gamecompanion.models

import kotlinx.serialization.SerialName

data class Twitch (
        @SerialName("id") val nameID : Int? = null,
        @SerialName("name")val name : String? = null,
        @SerialName("box_art_url") val boxArtUrl: String? = null

)