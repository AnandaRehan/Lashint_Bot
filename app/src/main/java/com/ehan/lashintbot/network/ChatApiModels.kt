package com.ehan.lashintbot.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatRequest(
    val message: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val reply: String
)