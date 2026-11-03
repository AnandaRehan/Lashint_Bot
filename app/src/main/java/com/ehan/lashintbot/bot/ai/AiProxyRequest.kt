package com.ehan.lashintbot.bot.ai

import com.google.gson.annotations.SerializedName

data class AiProxyRequest(
    @SerializedName("prompt") // 👈 Memaksa Retrofit mengirim JSON dengan key "prompt"
    val prompt: String
)
