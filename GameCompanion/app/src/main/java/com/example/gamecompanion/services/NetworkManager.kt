package com.example.gamecompanion.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

object NetworkManager {

    fun createHttpClient(): HttpClient{
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }

        return HttpClient(OkHttp){
            install(JsonFeature){
                serializer = KotlinxSerializer(jsonConfig)
            }
        }

    }

}