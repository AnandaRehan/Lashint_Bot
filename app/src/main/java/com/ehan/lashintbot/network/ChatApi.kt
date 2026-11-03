package com.ehan.lashintbot.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("api/ai")
    suspend fun ask(
        @Body request: ChatRequest
    ): ChatResponse
}