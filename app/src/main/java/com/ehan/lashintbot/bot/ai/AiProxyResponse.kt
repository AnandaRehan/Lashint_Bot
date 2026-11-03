package com.ehan.lashintbot.bot.ai

import com.google.gson.annotations.SerializedName

data class AiProxyResponse(
    @SerializedName("text") // 👈 WAJIB: Mengunci kunci JSON agar tetap "text"
    val text: String
)
